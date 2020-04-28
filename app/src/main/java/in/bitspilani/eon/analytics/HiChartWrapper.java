package in.bitspilani.eon.analytics;

import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.hichartsclasses.HICSSObject;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIColumn;
import com.highsoft.highcharts.common.hichartsclasses.HICondition;
import com.highsoft.highcharts.common.hichartsclasses.HICredits;
import com.highsoft.highcharts.common.hichartsclasses.HIDataLabels;
import com.highsoft.highcharts.common.hichartsclasses.HIExporting;
import com.highsoft.highcharts.common.hichartsclasses.HILabel;
import com.highsoft.highcharts.common.hichartsclasses.HILegend;
import com.highsoft.highcharts.common.hichartsclasses.HILine;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIResponsive;
import com.highsoft.highcharts.common.hichartsclasses.HIRules;
import com.highsoft.highcharts.common.hichartsclasses.HISeries;
import com.highsoft.highcharts.common.hichartsclasses.HIStackLabels;
import com.highsoft.highcharts.common.hichartsclasses.HISubtitle;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import in.bitspilani.eon.analytics.data.TicketGraphObject;

public class HiChartWrapper {

    public static HIOptions getOptionsForBar(TicketGraphObject ticketGraphObject) {

        HIOptions options = new HIOptions();
        HITitle title = new HITitle();
        title.setText("Remaining Tickets vs Sold Tickets");
        options.setTitle(title);

        HIExporting exporting = new HIExporting();
        exporting.setEnabled(false);
        options.setExporting(exporting);

        HIXAxis xaxis = new HIXAxis();
        xaxis.setCategories(ticketGraphObject.getName_list());
        options.setXAxis(new ArrayList<>(Collections.singletonList(xaxis)));

        HICredits credits = new HICredits();
        credits.setEnabled(false);
        options.setCredits(credits);


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

    public static HIOptions getOptionsForLine(TicketGraphObject ticketGraphObject){

        HIOptions options = new HIOptions();

        HIExporting exporting = new HIExporting();
        exporting.setEnabled(false);
        options.setExporting(exporting);


        HICredits credits = new HICredits();
        credits.setEnabled(false);
        options.setCredits(credits);


        HITitle title = new HITitle();
        title.setText("Event wise revenue");
        options.setTitle(title);

        HIYAxis yaxis = new HIYAxis();
        yaxis.setTitle(new HITitle());
        yaxis.getTitle().setText("Revenue");
        options.setYAxis(new ArrayList<>(Collections.singletonList(yaxis)));

        HIXAxis xaxis = new HIXAxis();
        xaxis.setTitle(new HITitle());
        xaxis.getTitle().setText("Events");
        xaxis.setCategories(ticketGraphObject.getName_list());
        options.setXAxis(new ArrayList<>(Collections.singletonList(xaxis)));

        HILegend legend = new HILegend();
        legend.setLayout("vertical");
        legend.setAlign("right");
        legend.setVerticalAlign("middle");
        options.setLegend(legend);

        HIPlotOptions plotoptions = new HIPlotOptions();
        plotoptions.setSeries(new HISeries());
        options.setPlotOptions(plotoptions);

        HILine line1 = new HILine();
        line1.setName("Revenue");
        line1.setData(new ArrayList<>(ticketGraphObject.getRevenue_list()));

        HIResponsive responsive = new HIResponsive();

        HIRules rules1 = new HIRules();
        rules1.setCondition(new HICondition());
        rules1.getCondition().setMaxWidth(500);
        HashMap<String, HashMap> chartLegend = new HashMap<>();
        HashMap<String, String> legendOptions = new HashMap<>();
        legendOptions.put("layout", "horizontal");
        legendOptions.put("align", "center");
        legendOptions.put("verticalAlign", "bottom");
        chartLegend.put("legend", legendOptions);
        rules1.setChartOptions(chartLegend);
        responsive.setRules(new ArrayList<>(Collections.singletonList(rules1)));
        options.setResponsive(responsive);

        options.setSeries(new ArrayList<>(Arrays.asList(line1)));

       return options;
    }
}
