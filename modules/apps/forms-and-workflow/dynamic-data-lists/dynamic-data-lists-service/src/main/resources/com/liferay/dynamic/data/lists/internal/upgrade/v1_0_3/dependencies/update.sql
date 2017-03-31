alter table DDLRecord add recordSetVersion VARCHAR(75) null;

alter table DDLRecordVersion add recordSetVersion VARCHAR(75) null;

create index IX_F12C61D4 on DDLRecord (recordSetId, recordSetVersion[$COLUMN_LENGTH:75$]);

create index IX_19AD75F6 on DDLRecordVersion (recordSetId, recordSetVersion[$COLUMN_LENGTH:75$]);