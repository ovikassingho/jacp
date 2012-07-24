/*
 * Copyright (C) 2010 - 2012.
 * AHCP Project (http://code.google.com/p/jacp)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jacp.demo.callbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.chart.XYChart;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.annotations.CallbackComponent;
import org.jacp.demo.entity.Contact;
import org.jacp.demo.entity.ContactDTO;
import org.jacp.demo.main.Util;
import org.jacp.javafx.rcp.component.AStatefulCallbackComponent;

/**
 * The AnalyticsCallbac components creates chart data (random data)
 * 
 * @author Andy Moncsek
 * 
 */
@CallbackComponent(id = "id006", name = "analyticsCallback", active = false)
public class AnalyticsCallback extends AStatefulCallbackComponent {
	private final Random rnd = new Random();

	@Override
	public Object handleAction(final IAction<Event, Object> action) {
		if (action.getLastMessage() instanceof Contact) {
			final Contact contact = (Contact) action.getLastMessage();
			this.creatAndSendData(contact);
		}
		return null;
	}

	/**
	 * for demo purpose send Contact with "electronic" statistic data and than
	 * all other
	 * 
	 * @param contact
	 */
	private void creatAndSendData(final Contact contact) {
		final ContactDTO dto = new ContactDTO();
		contact.setDto(dto);

		dto.setSeriesOneData(this.createChartData());
		dto.setSeriesTwoData(this.createChartData());
		dto.setSeriesThreeData(this.createChartData());
		dto.setSeriesFourData(this.createChartData());
		this.sendChartData(contact);

	}

	private void sendChartData(final Object data) {
		final IActionListener<EventHandler<Event>, Event, Object> listener = this
				.getActionListener("id01.id003", data);
		listener.performAction(null);
	}

	private List<XYChart.Data<String, Number>> createChartData() {
		final List<XYChart.Data<String, Number>> data = new ArrayList<XYChart.Data<String, Number>>();
		for (final String element : Util.YEARS) {
			data.add(new XYChart.Data<String, Number>(element, this.random()));
		}

		return data;
	}

	// Common but flawed!
	private int random() {
		return Math.abs(this.rnd.nextInt()) % 9999;
	}

}
