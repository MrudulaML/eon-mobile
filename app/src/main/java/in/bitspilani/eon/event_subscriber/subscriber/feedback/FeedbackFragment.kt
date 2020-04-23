package `in`.bitspilani.eon.event_subscriber.subscriber.feedback

import `in`.bitspilani.eon.BitsEonActivity
import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_subscriber.models.FeedbackBody
import `in`.bitspilani.eon.event_subscriber.models.FeedbackData
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.Constants
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_feedback.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File


class FeedbackFragment : Fragment() {

    private val feedbackViewmodel by viewModels<FeedbackViewmodel> { getViewModelFactory() }
    private var actionbarHost: ActionbarHost? = null

    lateinit var adapter: FeedbackAdapter

    var READ_PERMISSION: Int = 912
    var PICK_IMAGE: Int = 911
    var imageUri: Uri? = null

    var eventId: Int = 0
    var position: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionbarHost?.showToolbar(
            showToolbar = false,
            showBottomNav = false
        )

        setObservables()
        setClicks()
        init()

        feedbackViewmodel.getQuestions()
    }

    fun init() {

        eventId = arguments!!.getInt(Constants.EVENT_ID, 0)

        tv_event_name.text = arguments!!.getString(Constants.EVENT_NAME)

    }

    fun setClicks() {

        btn_submit.clickWithDebounce {

            feedbackViewmodel.postFeedback(FeedbackBody(eventId, list))
        }

        iv_back_subs_feed.clickWithDebounce {

            findNavController().popBackStack()
        }

    }

    var imageName: String = ""
    var list: ArrayList<FeedbackData> = ArrayList<FeedbackData>()
    fun setObservables() {


        //loader observable
        feedbackViewmodel.progressLiveData.observe(viewLifecycleOwner, Observer {

            (activity as BitsEonActivity).showProgress(it)
        })

        feedbackViewmodel.questionsData.observe(viewLifecycleOwner, Observer {


            list = it.data

            rv_subscriber_feedback.layoutManager = LinearLayoutManager(activity!!)
            rv_subscriber_feedback.adapter = FeedbackAdapter(list) {

                position = it
                openGallery()
            }
            rv_subscriber_feedback.setHasFixedSize(true)


        })

        feedbackViewmodel.presignData.observe(viewLifecycleOwner, Observer {

            imageName = it.data.image_name
            feedbackViewmodel.uploadImageToS3(it.data.presigned_url, getRequestBody(imageUri))

        })


        feedbackViewmodel.uploadImageData.observe(viewLifecycleOwner, Observer {

            list[position].imageUri = imageUri
            list[position].answer.image = imageName

            rv_subscriber_feedback.adapter!!.notifyItemChanged(position)

//            rv_subscriber_feedback.adapter!!.notifyDataSetChanged()

            showUserMsg("Image uploaded successfully")

        })

        feedbackViewmodel.errorData.observe(viewLifecycleOwner, Observer {
            showUserMsg(it)
        })

        feedbackViewmodel.postFeedbackData.observe(viewLifecycleOwner, Observer {

            showUserMsg("Feedback Submitted Successfully")
            findNavController().popBackStack()
        })

    }


    fun showUserMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
            .show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionbarHost) {
            actionbarHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        actionbarHost?.showToolbar(showToolbar = false, showBottomNav = false)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data!!.getData()

            Log.e("xoxo", "file name" + getRealPathFromURI(activity!!, imageUri!!))
            updateAdapter(imageUri)

        }

    }


    private fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            cursor.getString(column_index)
        } catch (e: Exception) {
            Log.e("xoxo", "getRealPathFromURI Exception : $e")
            ""
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }


    fun updateAdapter(imageUri: Uri?) {

        this.imageUri = imageUri
        val file = File(imageUri?.path)

        feedbackViewmodel.getPresignUrl(file.name)


//        list[position].imageUri = imageUri
//
//        rv_subscriber_feedback.adapter!!.notifyItemChanged(position)
    }


    fun getRequestBody(imageUri: Uri?): RequestBody {

        val file = File(getRealPathFromURI(activity!!, imageUri!!)!!)

        return RequestBody.create(MediaType.parse("image/jpeg"), file)
    }


    private fun openGallery() {

        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {

            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        } else {

            askPermissions()
        }

    }

    fun askPermissions() {

        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_PERMISSION
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            READ_PERMISSION -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    openGallery()
                } else {

                    showUserMsg("Please grant the necessary permission ")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


}