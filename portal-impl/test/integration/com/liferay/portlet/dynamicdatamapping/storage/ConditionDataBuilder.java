package com.liferay.portlet.dynamicdatamapping.storage;

import com.liferay.portlet.dynamicdatamapping.storage.query.ComparisonOperator;

import java.io.Serializable;

import java.util.List;
public class ConditionDataBuilder {

	public ComparisonOperator getComparisonOperator() {

		return _comparisonOperator;
	}

	public List<ExpectedResult> getExpectedResults() {

		return _expectedResults;
	}

	public String getFieldName() {

		return _fieldName;
	}

	public Serializable[] getSampleValues() {

		return _sampleValues;
	}

	public StorageAdapter getStorageAdapter() {

		return _storageAdapter;
	}

	public String getStructureName() {

		return _structureName;
	}

	public String getStructureSchema() {

		return _structureSchema;
	}

	public void setComparisonOperator(ComparisonOperator comparisonOperator) {

		this._comparisonOperator = comparisonOperator;
	}

	public void setExpectedResults(List<ExpectedResult> expectedResults) {

		this._expectedResults = expectedResults;
	}

	public void setFieldName(String fieldName) {

		this._fieldName = fieldName;
	}

	public void setSampleValues(Serializable[] sampleValues) {

		this._sampleValues = sampleValues;
	}

	public void setStorageAdapter(StorageAdapter storageAdapter) {

		this._storageAdapter = storageAdapter;
	}

	public void setStructureName(String structureName) {

		this._structureName = structureName;
	}

	public void setStructureSchema(String structureSchema) {

		this._structureSchema = structureSchema;
	}

	private StorageAdapter _storageAdapter; private ComparisonOperator _comparisonOperator;
	private List<ExpectedResult> _expectedResults;
	private String _fieldName;
	private Serializable[] _sampleValues;
	private String _structureName;
	private String _structureSchema;

}