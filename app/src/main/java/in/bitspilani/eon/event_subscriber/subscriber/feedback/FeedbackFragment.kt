package `in`.bitspilani.eon.event_subscriber.subscriber.feedback

import `in`.bitspilani.eon.R
import `in`.bitspilani.eon.event_subscriber.models.Feedback
import `in`.bitspilani.eon.login.ui.ActionbarHost
import `in`.bitspilani.eon.utils.clickWithDebounce
import `in`.bitspilani.eon.utils.getViewModelFactory
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_feedback.*


class FeedbackFragment : Fragment() {

    private val feedbackViewmodel by viewModels<FeedbackViewmodel> { getViewModelFactory() }
    private var actionbarHost: ActionbarHost? = null


    var PICK_IMAGE: Int = 911
    var imageUri: Uri? = null

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
            showToolbar = true,
            title = "Send Feedback",
            showBottomNav = false
        )

        setRecyclerview()

        setClicks()
    }

    fun setClicks() {

        btn_submit.clickWithDebounce {

            list.forEach{

                Log.e("xoxo", "heres my feedback : answer:"+it.answer+" image: "+it.imageUri+" question: "+it.question)
            }


        }

    }

    var list: ArrayList<Feedback> = ArrayList<Feedback>()
    fun setRecyclerview() {


        list.add(Feedback("Are you mad at me wehbelkb asjbdbask dbkasdbjbaskjdb?"))
        list.add(Feedback("Lets play holi akjbsdlbajsdb kbajsdbbasdb kjaksdbj baskbdbabsdbabsdkb?"))
        list.add(Feedback("Did you like our event kjabsdflkjjsbdkfsad askdjva?"))
        list.add(Feedback("Do you like eminem"))

        rv_subscriber_feedback.layoutManager = LinearLayoutManager(activity!!)
        rv_subscriber_feedback.adapter = FeedbackAdapter(list) {

            position = it
            openGallery()
        }


    }

    fun setObservables() {


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

            updateAdapter(imageUri)

        }

    }

    fun updateAdapter(imageUri: Uri?) {

        list[position].imageUri = imageUri

        rv_subscriber_feedback.adapter!!.notifyItemChanged(position)
    }

    private fun openGallery() {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }


}