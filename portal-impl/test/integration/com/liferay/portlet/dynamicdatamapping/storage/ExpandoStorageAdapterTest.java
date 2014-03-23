
package com.liferay.portlet.dynamicdatamapping.storage;

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.util.DLAppTestUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.storage.query.ComparisonOperator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
public class ExpandoStorageAdapterTest extends BaseStorageAdapterTest {

	@Override
	public StorageAdapter getStorageAdapter() {

		return _expandoStorageAdapater;
	}

	@Override
	public void testBooleanField() throws Exception {
		String xsd = readText("ddm-structure-boolean-field.xsd");

		DDMStructure structure = addStructure(_classNameId, null,
			"Boolean Field Structure", xsd,StorageType.EXPANDO.getValue(),
			DDMStructureConstants.TYPE_DEFAULT);

		Fields fields = new Fields();

		Map<Locale, List<Serializable>> dataMap =
			new HashMap<Locale, List<Serializable>>();

		List<Serializable> enValues = ListUtil.fromArray(new Serializable[] {
			true, true, true});

		dataMap.put(_enLocale, enValues);

		List<Serializable> ptValues = ListUtil.fromArray(new Serializable[] {
			false, false, false});

		dataMap.put(_ptLocale, ptValues);

		Field booleanField = new Field(structure.getStructureId(), "boolean",
			dataMap, _enLocale);

		fields.put(booleanField);

		validate(structure.getStructureId(), fields);
	}

	@Override
	public void testConditionEquals() throws Exception {

		ConditionData conditionData = ConditionData
				.newConditionData(ComparisonOperator.EQUALS, "text")
				.withStructureSchema("ddm-structure-text-field.xsd")
				.withStructureName("Text Field Structure")
				.usingStorageAdapter(_expandoStorageAdapater)
				.usingSampleValues(_enLocale, "a")
				.withExpectedResults("a", 1)
				.withExpectedResults("c", 0);

		testCondition(conditionData);
	}

	@Override
	public void testConditionEqualsWithLocale() throws Exception{
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionEqualsWithRepeatable() throws Exception{

		ConditionData conditionData = ConditionData
			.newConditionData(ComparisonOperator.EQUALS, "text")
			.withStructureSchema("ddm-structure-text-field.xsd")
			.withStructureName("Text Field Structure")
			.usingStorageAdapter(_expandoStorageAdapater)
			.usingSampleValues(_enLocale, "a", "b")
			.withExpectedResults("a", 1)
			.withExpectedResults("b", 1)
			.withExpectedResults("c", 0);

		testCondition(conditionData);
	}

	@Override
	public void testConditionExcludes() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionExcludesWithLocale() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionExcludesWithRepeatable() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	public static void main(String[] args) {

		String teste = "<?xml version='1.0' encoding='UTF-8'?><root available-locales=\"en_US\" default-locale=\"en_US\"><Data language-id=\"en_US\">a</Data></root>";

		System.out.println(teste.replaceAll("", ""));
	}

	@Override
	public void testConditionGreaterThan() throws Exception {
		ConditionData conditionData = ConditionData
				.newConditionData(ComparisonOperator.GREATER_THAN, "number")
				.withStructureSchema("ddm-structure-number-field.xsd")
				.withStructureName("Number Field Structure")
				.usingStorageAdapter(_expandoStorageAdapater)
				.usingSampleValues(_enLocale, 5)
				.withExpectedResults(4, 1)
				.withExpectedResults(7, 0);

		testCondition(conditionData);
	}

	@Override
	public void testConditionGreaterThanOrEqualTo() throws Exception {
		ConditionData conditionData = ConditionData .newConditionData(
			ComparisonOperator.GREATER_THAN_OR_EQUAL_TO, "number")
				.withStructureSchema("ddm-structure-number-field.xsd")
				.withStructureName("Number Field Structure")
				.usingStorageAdapter(_expandoStorageAdapater)
				.usingSampleValues(_enLocale, 5)
				.withExpectedResults(4, 1)
				.withExpectedResults(5, 1)
				.withExpectedResults(6, 0)
				.withExpectedResults(7, 0);

		testCondition(conditionData);
	}

	@Override
	public void testConditionGreaterThanOrEqualToWithLocale() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionGreaterThanOrEqualToWithRepeatable()
		throws Exception {

		ConditionData conditionData = ConditionData .newConditionData(
			ComparisonOperator.GREATER_THAN_OR_EQUAL_TO, "number")
				.withStructureSchema("ddm-structure-number-field.xsd")
				.withStructureName("Number Field Structure")
				.usingStorageAdapter(_expandoStorageAdapater)
				.usingSampleValues(_enLocale,5, 6)
				.withExpectedResults(4, 1)
				.withExpectedResults(5, 1)
				.withExpectedResults(6, 1)
				.withExpectedResults(7, 0);

		testCondition(conditionData);
	}

	@Override
	public void testConditionGreaterThanWithLocale() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionGreaterThanWithRepeatable() throws Exception {
		ConditionData conditionData = ConditionData
			.newConditionData(ComparisonOperator.GREATER_THAN, "number")
			.withStructureSchema("ddm-structure-number-field.xsd")
			.withStructureName("Number Field Structure")
			.usingStorageAdapter(_expandoStorageAdapater)
			.usingSampleValues(_enLocale, 5, 6)
			.withExpectedResults(4, 1)
			.withExpectedResults(7, 0);

	testCondition(conditionData);
	}

	@Override
	public void testConditionIn() throws Exception {
		ConditionData conditionData =
			ConditionData
				.newConditionData(ComparisonOperator.IN, "number")
				.withStructureSchema(
					"ddm-structure-number-field.xsd").withStructureName(
					"Number Field Structure").usingStorageAdapter(
					_expandoStorageAdapater).usingSampleValues(_enLocale, 5)
				.withExpectedResults(Arrays.asList(7, 8, 9, 10), 0)
				.withExpectedResults(Arrays.asList(1, 2, 3, 4, 5, 6), 1);

		testCondition(conditionData);
	}

	@Override
	public void testConditionIncludes() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionIncludesWithLocale() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionIncludesWithRepeatable() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionInWithLocale() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionInWithRepeatable() throws Exception {
		ConditionData conditionData =
			ConditionData
				.newConditionData(ComparisonOperator.IN, "number")
				.withStructureSchema(
					"ddm-structure-number-field.xsd").withStructureName(
					"Number Field Structure").usingStorageAdapter(
					_expandoStorageAdapater).usingSampleValues(_enLocale, 5, 6)
				.withExpectedResults(Arrays.asList(7, 8, 9, 10), 0)
				.withExpectedResults(Arrays.asList(1, 2, 3, 4, 5, 6), 1)
				.withExpectedResults(Arrays.asList(4, 6), 1)
				.withExpectedResults(Arrays.asList(4, 5), 1);

		testCondition(conditionData);
	}

	@Override
	public void testConditionJoin() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionJoinWithLocale() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionJoinWithRepeatable() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionLessThan() throws Exception {
		ConditionData conditionData =
			ConditionData
				.newConditionData(
					ComparisonOperator.LESS_THAN, "number")
				.withStructureSchema(
					"ddm-structure-number-field.xsd").withStructureName(
					"Number Field Structure").usingStorageAdapter(
					_expandoStorageAdapater).usingSampleValues(_enLocale, 5, 6)
				.withExpectedResults(
					7, 1).withExpectedResults(6, 1).withExpectedResults(5, 0)
				.withExpectedResults(4, 0);

		testCondition(conditionData);
	}

	@Override
	public void testConditionLessThanOrEqualTo() throws Exception {
		ConditionData conditionData =
			ConditionData
				.newConditionData(
					ComparisonOperator.LESS_THAN_OR_EQUAL_TO, "number")
				.withStructureSchema(
					"ddm-structure-number-field.xsd").withStructureName(
					"Number Field Structure").usingStorageAdapter(
					_expandoStorageAdapater).usingSampleValues(_enLocale, 5, 6)
				.withExpectedResults(
					7, 1).withExpectedResults(6, 1).withExpectedResults(5, 1)
				.withExpectedResults(4, 0);

		testCondition(conditionData);
	}

	@Override
	public void testConditionLessThanOrEqualToWithLocale() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionLessThanOrEqualToWithRepeatable()
		throws Exception {

		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionLessThanWithLocale() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionLessThanWithRepeatable() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionLike() throws Exception {
		ConditionData conditionData =
			ConditionData
				.newConditionData(ComparisonOperator.LIKE, "text")
				.withStructureSchema(
					"ddm-structure-text-field.xsd").withStructureName(
					"Text Field Structure")
				.usingStorageAdapter(_expandoStorageAdapater).usingSampleValues(
					_enLocale, "unitedstates").withExpectedResults("az", 0)
				.withExpectedResults(
					"ted", 1).withExpectedResults("tate", 1)
				.withExpectedResults("xyz", 0);

		testCondition(conditionData);
	}

	@Override
	public void testConditionLikeWithLocale() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionLikeWithRepeatable() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionNotEquals() throws Exception {
		ConditionData conditionData =
			ConditionData
				.newConditionData(
					ComparisonOperator.NOT_EQUALS, "text")
					.withStructureSchema("ddm-structure-text-field.xsd")
					.withStructureName("Text Field Structure")
					.usingStorageAdapter(_expandoStorageAdapater)
					.usingSampleValues(_enLocale, "a")
					.withExpectedResults("a", 0)
					.withExpectedResults("b", 1);

		testCondition(conditionData);
	}

	@Override
	public void testConditionNotEqualsWithLocale() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionNotEqualsWithRepeatable() throws Exception {
		ConditionData conditionData =
			ConditionData
				.newConditionData(
					ComparisonOperator.NOT_EQUALS, "text")
					.withStructureSchema("ddm-structure-text-field.xsd")
					.withStructureName("Text Field Structure")
					.usingStorageAdapter(_expandoStorageAdapater)
					.usingSampleValues(_enLocale,"a", "b")
					.withExpectedResults("a", 0)
					.withExpectedResults("b", 0)
					.withExpectedResults("c", 1);

		testCondition(conditionData);
	}

	@Override
	public void testConditionNotIn() throws Exception {
		ConditionData conditionData =
			ConditionData
				.newConditionData(ComparisonOperator.NOT_IN, "number")
				.withStructureSchema("ddm-structure-number-field.xsd")
				.withStructureName("Number Field Structure")
				.usingStorageAdapter(_expandoStorageAdapater)
				.usingSampleValues(_enLocale, 5)
				.withExpectedResults(Arrays.asList(7, 8, 9, 10), 1)
				.withExpectedResults(Arrays.asList(1, 2, 3, 4, 5, 6), 0);

		testCondition(conditionData);
	}

	@Override
	public void testConditionNotInWithLocale() throws Exception {
		Assert.fail("Not yet implemented !");
	}

	@Override
	public void testConditionNotInWithRepeatable() throws Exception {
		ConditionData conditionData =
			ConditionData
				.newConditionData(ComparisonOperator.NOT_IN, "number")
				.withStructureSchema("ddm-structure-number-field.xsd")
				.withStructureName("Number Field Structure")
				.usingStorageAdapter(_expandoStorageAdapater)
				.usingSampleValues(_enLocale,5, 6)
				.withExpectedResults(Arrays.asList(7, 8, 9, 10), 1)
				.withExpectedResults(Arrays.asList(1, 2, 3, 4, 5, 6), 0)
				.withExpectedResults(Arrays.asList(4, 6), 1)
				.withExpectedResults(Arrays.asList(4, 5), 1);

		testCondition(conditionData);
	}

	@Override
	public void testDateField() throws Exception {
		String xsd = readText("ddm-structure-date-field.xsd");

		DDMStructure structure = addStructure(_classNameId, null,
			"Date Field Structure", xsd, StorageType.EXPANDO.getValue(),
				DDMStructureConstants.TYPE_DEFAULT);

		Fields fields = new Fields();

		Map<Locale, List<Serializable>> dataMap =
			new HashMap<Locale, List<Serializable>>();

		Date date1 = PortalUtil
			.getDate(0, 1, 2013);
		Date date2 = PortalUtil
			.getDate(0, 2, 2013);

		List<Serializable> enValues = ListUtil
			.fromArray(new Serializable[] {
				date1, date2
			});

		dataMap
			.put(_enLocale, enValues);

		Date date3 = PortalUtil
			.getDate(0, 3, 2013);
		Date date4 = PortalUtil
			.getDate(0, 4, 2013);

		List<Serializable> ptValues = ListUtil
			.fromArray(new Serializable[] {
				date3, date4
			});

		dataMap
			.put(_ptLocale, ptValues);

		Field dateField = new Field(
			structure
				.getStructureId(), "date", dataMap, _enLocale);

		fields
			.put(dateField);

		validate(structure
			.getStructureId(), fields);
	}

	@Override
	public void testDecimalField() throws Exception {
		String xsd = readText("ddm-structure-decimal-field.xsd");

		DDMStructure structure =
			addStructure(
				_classNameId, null, "Decimal Field Structure", xsd,
				StorageType.EXPANDO
					.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		Fields fields = new Fields();

		Map<Locale, List<Serializable>> dataMap =
			new HashMap<Locale, List<Serializable>>();

		List<Serializable> enValues = ListUtil
			.fromArray(new Serializable[] {
				1.1, 1.2, 1.3
			});

		dataMap
			.put(_enLocale, enValues);

		List<Serializable> ptValues = ListUtil
			.fromArray(new Serializable[] {
				2.1, 2.2, 2.3
			});

		dataMap
			.put(_ptLocale, ptValues);

		Field decimalField =
			new Field(structure
				.getStructureId(), "decimal", dataMap, _enLocale);

		fields
			.put(decimalField);

		validate(structure
			.getStructureId(), fields);
	}

	@Override
	public void testDocLibraryField() throws Exception {
		String xsd = readText("ddm-structure-doc-lib-field.xsd");

		DDMStructure structure =
			addStructure(
				_classNameId, null, "Documents and Media Field Structure", xsd,
				StorageType.EXPANDO
					.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		Fields fields = new Fields();

		Map<Locale, List<Serializable>> dataMap =
			new HashMap<Locale, List<Serializable>>();

		FileEntry file1 =
			DLAppTestUtil
				.addFileEntry(
					TestPropsValues
						.getGroupId(),
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, true,
					"Test 1.txt");

		String file1Value = getDocLibraryFieldValue(file1);

		FileEntry file2 =
			DLAppTestUtil
				.addFileEntry(
					TestPropsValues
						.getGroupId(),
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, true,
					"Test 2.txt");

		String file2Value = getDocLibraryFieldValue(file2);

		List<Serializable> enValues = ListUtil
			.fromArray(new Serializable[] {
				file1Value, file2Value
			});

		dataMap
			.put(_enLocale, enValues);

		List<Serializable> ptValues = ListUtil
			.fromArray(new Serializable[] {
				file1Value
			});

		dataMap
			.put(_ptLocale, ptValues);

		Field documentLibraryField =
			new Field(
				structure
					.getStructureId(), "doc_library", dataMap, _enLocale);

		fields
			.put(documentLibraryField);

		validate(structure
			.getStructureId(), fields);
	}

	@Override
	public void testIntegerField() throws Exception {
		String xsd = readText("ddm-structure-integer-field.xsd");

		DDMStructure structure =
			addStructure(
				_classNameId, null, "Integer Field Structure", xsd,
				StorageType.EXPANDO
					.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		Fields fields = new Fields();

		Map<Locale, List<Serializable>> dataMap =
			new HashMap<Locale, List<Serializable>>();

		List<Serializable> enValues = ListUtil
			.fromArray(new Serializable[] {
				1, 2, 3
			});

		dataMap
			.put(_enLocale, enValues);

		List<Serializable> ptValues = ListUtil
			.fromArray(new Serializable[] {
				3, 4, 5
			});

		dataMap
			.put(_ptLocale, ptValues);

		Field integerField =
			new Field(structure
				.getStructureId(), "integer", dataMap, _enLocale);

		fields
			.put(integerField);

		validate(structure
			.getStructureId(), fields);
	}

	@Override
	public void testLinkToPageField() throws Exception {
		String xsd = readText("ddm-structure-link-to-page-field.xsd");

		DDMStructure structure =
			addStructure(
				_classNameId, null, "Link to Page Field Structure", xsd,
				StorageType.EXPANDO
					.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		Fields fields = new Fields();

		Map<Locale, List<Serializable>> dataMap =
			new HashMap<Locale, List<Serializable>>();

		List<Serializable> enValues = ListUtil
			.fromArray(new Serializable[] {
				"{\"layoutId\":\"1\",\"privateLayout\":false}"
			});

		dataMap
			.put(_enLocale, enValues);

		List<Serializable> ptValues = ListUtil
			.fromArray(new Serializable[] {
				"{\"layoutId\":\"2\",\"privateLayout\":true}"
			});

		dataMap
			.put(_ptLocale, ptValues);

		Field linkToPageField =
			new Field(
				structure
					.getStructureId(), "link_to_page", dataMap, _enLocale);

		fields
			.put(linkToPageField);

		validate(structure
			.getStructureId(), fields);
	}

	@Override
	public void testNumberField() throws Exception {
		String xsd = readText("ddm-structure-number-field.xsd");

		DDMStructure structure =
			addStructure(
				_classNameId, null, "Number Field Structure", xsd,
				StorageType.EXPANDO
					.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		Fields fields = new Fields();

		Map<Locale, List<Serializable>> dataMap =
			new HashMap<Locale, List<Serializable>>();

		List<Serializable> enValues = ListUtil
			.fromArray(new Serializable[] {
				1, 1.5f, 2
			});

		dataMap
			.put(_enLocale, enValues);

		List<Serializable> ptValues = ListUtil
			.fromArray(new Serializable[] {
				3, 3.5f, 4
			});

		dataMap
			.put(_ptLocale, ptValues);

		Field numberField = new Field(
			structure
				.getStructureId(), "number", dataMap, _enLocale);

		fields
			.put(numberField);

		validate(structure
			.getStructureId(), fields);
	}

	@Override
	public void testRadioField() throws Exception {
		String xsd = readText("ddm-structure-radio-field.xsd");

		DDMStructure structure =
			addStructure(
				_classNameId, null, "Radio Field Structure", xsd,
				StorageType.EXPANDO
					.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		Fields fields = new Fields();

		Map<Locale, List<Serializable>> dataMap =
			new HashMap<Locale, List<Serializable>>();

		List<Serializable> enValues = ListUtil
			.fromArray(new Serializable[] {
				"[\"value 1\"]", "[\"value 2\"]"
			});

		dataMap
			.put(_enLocale, enValues);

		List<Serializable> ptValues = ListUtil
			.fromArray(new Serializable[] {
				"[\"value 2\"]", "[\"value 3\"]"
			});

		dataMap
			.put(_ptLocale, ptValues);

		Field radioField = new Field(
			structure
				.getStructureId(), "radio", dataMap, _enLocale);

		fields
			.put(radioField);

		validate(structure
			.getStructureId(), fields);
	}

	@Override
	public void testSelectField() throws Exception {
		String xsd = readText("ddm-structure-select-field.xsd");

		DDMStructure structure =
			addStructure(
				_classNameId, null, "Select Field Structure", xsd,
				StorageType.EXPANDO
					.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		Fields fields = new Fields();

		Map<Locale, List<Serializable>> dataMap =
			new HashMap<Locale, List<Serializable>>();

		List<Serializable> enValues = ListUtil
			.fromArray(new Serializable[] {
				"[\"value 1\",\"value 2\"]", "[\"value 3\"]"
			});

		dataMap
			.put(_enLocale, enValues);

		List<Serializable> ptValues = ListUtil
			.fromArray(new Serializable[] {
				"[\"value 2\"]", "[\"value 3\"]"
			});

		dataMap
			.put(_ptLocale, ptValues);

		Field selectField = new Field(
			structure
				.getStructureId(), "select", dataMap, _enLocale);

		fields
			.put(selectField);

		validate(structure
			.getStructureId(), fields);
	}

	@Override
	public void testTextField() throws Exception {
		String xsd = readText("ddm-structure-text-field.xsd");

		DDMStructure structure =
			addStructure(
				_classNameId, null, "Text Field Structure", xsd, StorageType.EXPANDO
					.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		Fields fields = new Fields();

		Map<Locale, List<Serializable>> dataMap =
			new HashMap<Locale, List<Serializable>>();

		List<Serializable> enValues = ListUtil
			.fromArray(new Serializable[] {
				"one", "two", "three"
			});

		dataMap
			.put(_enLocale, enValues);

		List<Serializable> ptValues = ListUtil
			.fromArray(new Serializable[] {
				"um", "dois", "tres"
			});

		dataMap
			.put(_ptLocale, ptValues);

		Field textField = new Field(
			structure
				.getStructureId(), "text", dataMap, _enLocale);

		fields
			.put(textField);

		validate(structure
			.getStructureId(), fields);
	}

	protected StorageAdapter _expandoStorageAdapater = new ExpandoStorageAdapter();

}