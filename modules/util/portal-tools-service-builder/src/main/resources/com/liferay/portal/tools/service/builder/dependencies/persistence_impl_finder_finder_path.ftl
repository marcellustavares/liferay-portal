<#assign entityColumns = entityFinder.entityColumns />

<#if entityFinder.isCollection()>
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_${entityFinder.name?upper_case} = new FinderPath(
		${entity.name}ModelImpl.ENTITY_CACHE_ENABLED,
		${entity.name}ModelImpl.FINDER_CACHE_ENABLED,
		${entity.name}Impl.class,
		FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
		"findBy${entityFinder.name}",
		new String[] {
			<#list entityColumns as entityColumn>
				${serviceBuilder.getPrimitiveObj("${entityColumn.type}")}.class.getName(),
			</#list>

			Integer.class.getName(), Integer.class.getName(), OrderByComparator.class.getName()
		});

	<#if !entityFinder.hasCustomComparator()>
		public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_${entityFinder.name?upper_case} = new FinderPath(
			${entity.name}ModelImpl.ENTITY_CACHE_ENABLED,
			${entity.name}ModelImpl.FINDER_CACHE_ENABLED,
			${entity.name}Impl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findBy${entityFinder.name}",
			new String[] {
				<#list entityColumns as entityColumn>
					${serviceBuilder.getPrimitiveObj("${entityColumn.type}")}.class.getName()

					<#if entityColumn_has_next>
						,
					</#if>
				</#list>
			}

			<#if columnBitmaskEnabled>
				,

				<#list entityColumns as entityColumn>
					${entity.name}ModelImpl.${entityColumn.name?upper_case}_COLUMN_BITMASK

					<#if entityColumn_has_next>
						|
					</#if>
				</#list>

				<#if entity.entityOrder??>
					<#list entity.entityOrder.entityColumns as entityColumn>
						<#if !entityColumns?seq_contains(entityColumn) && !entity.PKEntityColumns?seq_contains(entityColumn)>
							| ${entity.name}ModelImpl.${entityColumn.name?upper_case}_COLUMN_BITMASK
						</#if>
					</#list>
				</#if>
			</#if>

			);
	</#if>
</#if>

<#if !entityFinder.isCollection() || entityFinder.isUnique()>
	public static final FinderPath FINDER_PATH_FETCH_BY_${entityFinder.name?upper_case} = new FinderPath(
		${entity.name}ModelImpl.ENTITY_CACHE_ENABLED,
		${entity.name}ModelImpl.FINDER_CACHE_ENABLED,
		${entity.name}Impl.class,
		FINDER_CLASS_NAME_ENTITY,
		"fetchBy${entityFinder.name}",
		new String[] {
			<#list entityColumns as entityColumn>
				${serviceBuilder.getPrimitiveObj("${entityColumn.type}")}.class.getName()

				<#if entityColumn_has_next>
					,
				</#if>
			</#list>
		}

		<#if columnBitmaskEnabled>
			,

			<#list entityColumns as entityColumn>
				${entity.name}ModelImpl.${entityColumn.name?upper_case}_COLUMN_BITMASK

				<#if entityColumn_has_next>
					|
				</#if>
			</#list>
		</#if>

		);
</#if>

<#if !entityFinder.hasCustomComparator()>
	public static final FinderPath FINDER_PATH_COUNT_BY_${entityFinder.name?upper_case} = new FinderPath(
		${entity.name}ModelImpl.ENTITY_CACHE_ENABLED,
		${entity.name}ModelImpl.FINDER_CACHE_ENABLED,
		Long.class,
		FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
		"countBy${entityFinder.name}",
		new String[] {
			<#list entityColumns as entityColumn>
				${serviceBuilder.getPrimitiveObj("${entityColumn.type}")}.class.getName()

				<#if entityColumn_has_next>
					,
				</#if>
			</#list>
		});
</#if>

<#if entityFinder.hasArrayableOperator() || entityFinder.hasCustomComparator()>
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_${entityFinder.name?upper_case} = new FinderPath(
		${entity.name}ModelImpl.ENTITY_CACHE_ENABLED,
		${entity.name}ModelImpl.FINDER_CACHE_ENABLED,
		Long.class,
		FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
		"countBy${entityFinder.name}",
		new String[] {
			<#list entityColumns as entityColumn>
				${serviceBuilder.getPrimitiveObj("${entityColumn.type}")}.class.getName()

				<#if entityColumn_has_next>
					,
				</#if>
			</#list>
		});
</#if>