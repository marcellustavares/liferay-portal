package ${apiPackagePath}.model;

import aQute.bnd.annotation.ProviderType;

import java.sql.Blob;

/**
 * The Blob model class for lazy loading the ${column.name} column in ${entity.name}.
 *
 * @author ${author}
 * @see ${entity.name}
 * @generated
 */
@ProviderType
public class ${entity.name}${column.methodName}BlobModel {

	public ${entity.name}${column.methodName}BlobModel() {
	}

	<#assign pkEntityColumn = entity.PKEntityColumns?first />

	public ${entity.name}${column.methodName}BlobModel(
		${pkEntityColumn.type} ${pkEntityColumn.name}) {

		_${pkEntityColumn.name} = ${pkEntityColumn.name};
	}

	public ${entity.name}${column.methodName}BlobModel(
		${pkEntityColumn.type} ${pkEntityColumn.name}, Blob ${column.name}Blob) {

		_${pkEntityColumn.name} = ${pkEntityColumn.name};
		_${column.name}Blob = ${column.name}Blob;
	}

	public ${entity.PKClassName} get${pkEntityColumn.methodName}() {
		return _${entity.PKVarName};
	}

	public void set${pkEntityColumn.methodName}(${entity.PKClassName} ${entity.PKVarName}) {
		_${entity.PKVarName} = ${entity.PKVarName};
	}

	public Blob get${column.methodName}Blob() {
		return _${column.name}Blob;
	}

	public void set${column.methodName}Blob(Blob ${column.name}Blob) {
		_${column.name}Blob = ${column.name}Blob;
	}

	<#if entity.hasCompoundPK()>
		private ${entity.PKClassName} _${entity.PKVarName};
	<#else>
		private ${pkEntityColumn.type} _${pkEntityColumn.name};
	</#if>

	private Blob _${column.name}Blob;

}