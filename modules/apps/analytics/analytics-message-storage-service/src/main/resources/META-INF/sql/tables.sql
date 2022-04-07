create table AnalyticsMessage (
	mvccVersion LONG default 0 not null,
	analyticsMessageId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	body BLOB
);

create table AnalyticsModelRemoveLogger (
	mvccVersion LONG default 0 not null,
	analyticsModelRemoveLoggerId LONG not null primary key,
	companyId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	className VARCHAR(75) null,
	classPK LONG
);