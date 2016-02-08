select * from ADDRESS;
select count(*) as NUMBER_OF_ADDRESSES from ADDRESS;

select * from USERID;
select count(*) as NUMBER_OF_USERES from USERID;

select * from USERSESSION;
select count(*) as NUMBER_OF_SESSIONS from USERSESSION;

select * from URISTEAM;
select * from URISTEAM 
  where FILE like '/hlace%';

select * from USERSESSION_PAGEVIEW;

select * from PAGEVIEW;

select * from PROTOCOL;

select USERSESSION.ID as SESSIONID, ADDRESS.ADDRESS as IP, URISTEAM.FILE, URIQUERYPAIR.VALUE as QUERYVALUE, URIQUERYKEY.NAME as QUERYKEY, 
      REQUEST.DATE, REQUEST.TIME, REQUEST.SIZE_OF_RESPONSE, PROTOCOL.PROTOCOL, PROTOCOL.VERSION, REQUEST.STATUS_CODE
  from USERID 
  join ADDRESS on USERID.ADDRESS_ID = ADDRESS.id
  join USERSESSION on USERID.ID = USERSESSION.USERID_ID
  join USERSESSION_PAGEVIEW on USERSESSION.ID = USERSESSION_PAGEVIEW.USERSESSION_ID
  join PAGEVIEW on USERSESSION_PAGEVIEW.PAGES_ID = PAGEVIEW.ID
  join PAGEVIEW_REQUEST on PAGEVIEW.ID = PAGEVIEW_REQUEST.PAGEVIEW_ID
  join REQUEST on PAGEVIEW_REQUEST.REQUESTS_ID = REQUEST.ID
  join PROTOCOL on REQUEST.PROTOCOL_ID = PROTOCOL.ID
  join URISTEAMQUERY on REQUEST.STEAMQUERY_ID = URISTEAMQUERY.ID
  join URISTEAM on URISTEAMQUERY.URISTEAM_ID = URISTEAM.ID
  join URIQUERY on URISTEAMQUERY.QUERY_ID = URIQUERY.ID
  join URIQUERY_URIQUERYPAIR on URIQUERY.ID = URIQUERY_URIQUERYPAIR.URIQUERY_ID
  join URIQUERYPAIR on URIQUERY_URIQUERYPAIR.PAIRS_ID = URIQUERYPAIR.ID
  join URIQUERYKEY on URIQUERYPAIR.KEY_ID = URIQUERYKEY.ID
  where ADDRESS.ADDRESS like '157.55.39.110'
  order by USERSESSION.ID asc, REQUEST.DATE asc, REQUEST.TIME asc;

select * from USERAGENT;
select count(*) as NUMBER_OF_AGENTS from USERAGENT;

select URIQUERYPAIR.VALUE, URIQUERYKEY.NAME as KEY
  from URIQUERYPAIR
  join URIQUERYKEY on URIQUERYPAIR.KEY_ID = URIQUERYKEY.ID;
  
select * from REQUEST;
