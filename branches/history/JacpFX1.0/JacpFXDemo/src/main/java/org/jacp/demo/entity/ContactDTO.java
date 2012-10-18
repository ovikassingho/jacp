/*
 * Copyright (C) 2010,2011.
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
package org.jacp.demo.entity;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

public class ContactDTO {
	private String parentName;
	private int amount;
	private ObservableList<Contact> contacts = FXCollections
			.observableArrayList();
	private List<XYChart.Data<String, Number>> seriesOneData;
	private List<XYChart.Data<String, Number>> seriesTwoData;
	private List<XYChart.Data<String, Number>> seriesThreeData;
	private List<XYChart.Data<String, Number>> seriesFourData;
	
	public ContactDTO(String parentName,int amount) {
		this.parentName = parentName;
		this.amount = amount;
	}
	
	public ContactDTO() {
		// TODO Auto-generated constructor stub
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public ObservableList<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(ObservableList<Contact> contacts) {
		this.contacts = contacts;
	}

	public List<XYChart.Data<String, Number>> getSeriesOneData() {
		return seriesOneData;
	}

	public void setSeriesOneData(List<XYChart.Data<String, Number>> seriesOneData) {
		this.seriesOneData = seriesOneData;
	}

	public List<XYChart.Data<String, Number>> getSeriesTwoData() {
		return seriesTwoData;
	}

	public void setSeriesTwoData(List<XYChart.Data<String, Number>> seriesTwoData) {
		this.seriesTwoData = seriesTwoData;
	}

	public List<XYChart.Data<String, Number>> getSeriesThreeData() {
		return seriesThreeData;
	}

	public void setSeriesThreeData(
			List<XYChart.Data<String, Number>> seriesThreeData) {
		this.seriesThreeData = seriesThreeData;
	}

	public List<XYChart.Data<String, Number>> getSeriesFourData() {
		return seriesFourData;
	}

	public void setSeriesFourData(List<XYChart.Data<String, Number>> seriesFourData) {
		this.seriesFourData = seriesFourData;
	}

}
