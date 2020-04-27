package in.bitspilani.eon.analytics;

import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.hichartsclasses.HICSSObject;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIColumn;
import com.highsoft.highcharts.common.hichartsclasses.HIDataLabels;
import com.highsoft.highcharts.common.hichartsclasses.HILegend;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIStackLabels;
import com.highsoft.highcharts.common.hichartsclasses.HISubtitle;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import in.bitspilani.eon.analytics.data.TicketGraphObject;

public class JavaUtilsForBar {

    public static HIOptions getOptions(TicketGraphObject ticketGraphObject) {

        HIOptions options = new HIOptions();

        HITitle title = new HITitle();
        title.setText("Remaining Tickets vs Sold Tickets");
        options.setTitle(title);


        HIXAxis xaxis = new HIXAxis();
        xaxis.setCategories(ticketGraphObject.getName_list());
        options.setXAxis(new ArrayList<>(Collections.singletonList(xaxis)));


        HIYAxis yaxis = new HIYAxis();
        yaxis.setMin(0);
        yaxis.setTitle(new HITitle());
        yaxis.getTitle().setText("Total Tickets");
        yaxis.setStackLabels(new HIStackLabels());
        yaxis.getStackLabels().setEnabled(true);
        yaxis.getStackLabels().setStyle(new HICSSObject());
        yaxis.getStackLabels().getStyle().setFontWeight("bold");
        yaxis.getStackLabels().getStyle().setColor("gray");
        options.setYAxis(new ArrayList<>(Collections.singletonList(yaxis)));

        HILegend legend = new HILegend();
        legend.setAlign("right");
        legend.setX(-30);
        legend.setVerticalAlign("top");
        legend.setY(25);
        legend.setFloating(true);
        legend.setBackgroundColor(HIColor.initWithName("white"));
        legend.setBorderColor(HIColor.initWithHexValue("ccc"));
        legend.setBorderWidth(1);
        legend.setShadow(false);
        options.setLegend(legend);

        HITooltip tooltip = new HITooltip();
        tooltip.setPointFormat("{series.name}: {point.y}<br/>Total: {point.stackTotal}");
        tooltip.setHeaderFormat("<b>{point.x}</b><br/>");
        options.setTooltip(tooltip);

        HIPlotOptions plotoptions = new HIPlotOptions();
        plotoptions.setColumn(new HIColumn());
        plotoptions.getColumn().setStacking("normal");
        plotoptions.getColumn().setDataLabels(new HIDataLabels());
        plotoptions.getColumn().getDataLabels().setEnabled(true);
        plotoptions.getColumn().getDataLabels().setColor(HIColor.initWithName("white"));
        plotoptions.getColumn().getDataLabels().setStyle(new HICSSObject());
        plotoptions.getColumn().getDataLabels().getStyle().setTextOutline("0 0 3px black");
        options.setPlotOptions(plotoptions);

        HIColumn column1 = new HIColumn();
        column1.setName("Remaining Tickets");
        column1.setData(ticketGraphObject.getRemaining_tickets());

        HIColumn column2 = new HIColumn();
        column2.setName("Sold Tickets");
        column2.setData(ticketGraphObject.getSold_tickets());

        HIColumn column3 = new HIColumn();
        column3.setData(ticketGraphObject.getSold_tickets());

        options.setSeries(new ArrayList<>(Arrays.asList(column1, column2)));

       return options;

    }
}
