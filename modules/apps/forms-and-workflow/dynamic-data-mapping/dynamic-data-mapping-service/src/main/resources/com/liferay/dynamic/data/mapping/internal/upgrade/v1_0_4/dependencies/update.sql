alter table DDMStructureVersion add lastPublishDate DATE null;
alter table DDMStructureVersion add modifiedDate DATE null;
alter table DDMStructureVersion add uuid_ VARCHAR(75) null;

create index IX_54DF650C on DDMStructureVersion (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_6CD5BE8E on DDMStructureVersion (uuid_[$COLUMN_LENGTH:75$], groupId);

COMMIT_TRANSACTION;