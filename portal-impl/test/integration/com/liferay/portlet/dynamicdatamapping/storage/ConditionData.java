
package com.liferay.portlet.dynamicdatamapping.storage;

import com.liferay.portlet.dynamicdatamapping.storage.query.ComparisonOperator;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
public class ConditionData {

	public static ConditionData newConditionData(
		ComparisonOperator comparisonOperator, String fieldName)
	{

		return new ConditionData(comparisonOperator, fieldName);
	}

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

	public ConditionData usingSampleValues(Serializable... sampleValues) {

		this._sampleValues = sampleValues;

		return this;
	}

	public ConditionData usingStorageAdapter(StorageAdapter storageAdapter) {

		this._storageAdapter = storageAdapter;

		return this;
	}

	public ConditionData withExpectedResults(Object _valueForTest,
		int _resultCount)
	{

		this._expectedResults
			.add(new ExpectedResult(_comparisonOperator, _fieldName,
				_valueForTest, _resultCount));

		return this;
	}

	public ConditionData withStructureName(String structureName) {

		this._structureName = structureName;

		return this;
	}

	public ConditionData withStructureSchema(String structureSchema) {

		this._structureSchema = structureSchema;

		return this;
	}

	private ConditionData(ComparisonOperator comparisonOperator,
		String fieldName)
	{

		this._fieldName = fieldName;
		this._comparisonOperator = comparisonOperator;
		this._expectedResults = new ArrayList<ExpectedResult>();
	}

	private ComparisonOperator _comparisonOperator;
	private List<ExpectedResult> _expectedResults;
	private String _fieldName;
	private Serializable[] _sampleValues;
	private StorageAdapter _storageAdapter;
	private String _structureName;
	private String _structureSchema;

}