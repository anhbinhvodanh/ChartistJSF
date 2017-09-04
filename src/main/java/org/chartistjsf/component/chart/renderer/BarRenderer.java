package org.chartistjsf.component.chart.renderer;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.chartistjsf.component.chart.Chart;
import org.chartistjsf.model.chart.Axis;
import org.chartistjsf.model.chart.AxisType;
import org.chartistjsf.model.chart.BarChartModel;
import org.chartistjsf.model.chart.ChartSeries;
import org.primefaces.util.ComponentUtils;

public class BarRenderer extends BaseChartistRenderer {

	private static final Logger logger = Logger.getLogger(BarRenderer.class.getName());

	@Override
	protected void encodeData(FacesContext context, Chart chart) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		BarChartModel model = (BarChartModel) chart.getModel();

		if (model.getLabels().isEmpty()) {
			logger.log(Level.SEVERE,
					"Make sure to set the required lables for BarChart, otherwise the chart won't render");
			return;
		}

		writer.write(",data:{");

		writer.write("labels: [");
		for (Iterator<Object> labelsItr = model.getLabels().iterator(); labelsItr.hasNext();) {
			Object label = labelsItr.next();

			if (label instanceof String)
				writer.write("\"" + ComponentUtils.escapeText(label.toString()) + "\"");
			else
				writer.write(label.toString());

			if (labelsItr.hasNext()) {
				writer.write(",");
			}
		}
		writer.write("],");

		writer.write(" series:[");
		for (Iterator<ChartSeries> it = model.getSeries().iterator(); it.hasNext();) {
			ChartSeries series = it.next();
			writer.write("{");
			writer.write("name:\"" + ComponentUtils.escapeText(series.getName()) + "\"");
			writer.write(", data:[");
			for (Iterator<Number> numbersIter = series.getData().iterator(); numbersIter.hasNext();) {
				Number number = numbersIter.next();
				String numberAsString = (number != null) ? number.toString() : "null";

				writer.write(numberAsString);

				if (numbersIter.hasNext()) {
					writer.write(",");
				}

			}
			writer.write("]");

			// className for series
			if (series.getClassName() != null && !series.getClassName().isEmpty()) {
				writer.write(", className:\"" + series.getClassName() + "\"");
			}
			writer.write("}");

			if (it.hasNext()) {
				writer.write(",");
			}
		}
		writer.write("]");
		writer.write("}");
	}

	@Override
	protected void encodeOptions(FacesContext context, Chart chart) throws IOException {
		super.encodeOptions(context, chart);

		ResponseWriter writer = context.getResponseWriter();
		BarChartModel model = (BarChartModel) chart.getModel();
		writer.write(",animateAdvanced:" + model.isAnimateAdvanced());
		writer.write(",animatePath:" + model.isAnimatePath());
		writer.write(",options:{");
		for (Iterator<AxisType> it = model.getAxes().keySet().iterator(); it.hasNext();) {
			AxisType axisType = it.next();
			Axis axis = model.getAxes().get(axisType);
			axis.render(writer, axisType);
			if (it.hasNext()) {
				writer.write(",");
			}
		}
		if (model.getWidth() != null)
			writer.write(",width:\"" + ComponentUtils.escapeText(model.getWidth()) + "\"");

		if (model.getHeight() != null)
			writer.write(",height:\"" + ComponentUtils.escapeText(model.getHeight()) + "\"");

		writer.write(",seriesBarDistance:" + model.getSeriesBarDistance());
		writer.write(",stackBars:" + model.isStackBars());
		writer.write(",horizontalBars:" + model.isHorizontalBars());
		writer.write(",showGridBackground:" + model.isShowGridBackground());

		if (model.getLow() != 0)
			writer.write(",low:" + model.getLow());

		if (model.getHigh() != 0)
			writer.write(",high:" + model.getHigh());

		if (model.getChartPadding() != null)
			writer.write(",chartPadding:" + model.getChartPadding());

		writer.write(",reverseData:" + model.isReverseData());

		if (model.getPlugins() != null)
			writer.write(",plugins:" + model.getPlugins());
		else if (chart.getPlugins() != null)
			writer.write(",plugins:" + chart.getPlugins());

		writer.write("}");
	}

}
