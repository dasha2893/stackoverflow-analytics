    CREATE TABLE users
(
  id integer NOT NULL,
  reputation integer,
  creationDate timestamp without time zone,
  displayName character varying(40),
  lastAccessDate timestamp without time zone,
  websiteUrl character varying(200),
  location character varying(100),
  aboutMe character varying,
  views integer,
  upVotes integer,
  downVotes integer,
  profileImageUrl character varying(200),
  emailHash character varying(32),
  age integer,
  accountId integer
);


CREATE TABLE postTypes
(
  id smallint NOT NULL,
  name character varying(50)
);


CREATE TABLE posts
(
  id integer NOT NULL,
  postTypeId smallint,
  acceptedAnswerId integer,
  parentId integer,
  creationDate timestamp without time zone,
  score integer,
  viewCount integer,
  body character varying,
  ownerUserId integer,
  ownerDisplayName character varying(40),
  lastEditorUserId integer,
  lastEditorDisplayName character varying(40),
  lastEditDate timestamp without time zone,
  lastActivityDate timestamp without time zone,
  title character varying(250),
  tags character varying(150),
  answerCount integer,
  commentCount integer,
  favoriteCount integer,
  closedDate timestamp without time zone,
  communityOwnedDate timestamp without time zone
);


CREATE TABLE voteTypes
(
  id smallint NOT NULL,
  name character varying(50)
);


CREATE TABLE votes
(
  id integer NOT NULL,
  postId integer,
  voteTypeId smallint,
  userId integer,
  creationDate timestamp without time zone,
  bountyAmount integer

);


CREATE TABLE badges
(
  id integer NOT NULL,
  userId integer,
  name character varying(50),
  date timestamp without time zone

);


CREATE TABLE comments
(
  id integer NOT NULL,
  postId integer,
  score integer,
  text character varying(600),
  creationDate timestamp without time zone,
  userDisplayName character varying(30),
  userId integer

);


CREATE TABLE postHistoryTypes
(
  id smallint NOT NULL,
  name character varying(50)
);


CREATE TABLE postHistory
(
  id integer NOT NULL,
  postHistoryTypeId smallint,
  postId integer,
  revisionGUID uuid,
  creationDate timestamp without time zone,
  userId integer,
  UserDisplayName character varying(40),
  comment character varying(400),
  text character varying

);



CREATE TABLE postHistoryTypes
(
  id smallint NOT NULL,
  name character varying(50)
);


CREATE TABLE tags
(
  id integer NOT NULL,
  tagName character varying(25),
  count integer,
  excerptPostId integer,
  wikiPostId integer
);


CREATE TABLE postLinks
(
  id integer NOT NULL,
  creationDate timestamp without time zone,
  postId integer,
  relatedPostId integer,
  linkTypeId smallint
);


CREATE TABLE fact_count_registered_users
(
  count_registered_users integer,
  id_users integer NOT NULL

);


CREATE TABLE dates
(
  datekey serial NOT NULL,
  year double precision,
  month double precision,
  day double precision
);


CREATE TABLE users_dim
(
  id integer NOT NULL,
  reputation integer,
  creation_date_key integer,
  displayName character varying(40),
  location character varying(100),
  views integer,
  upVotes integer,
  downVotes integer,
  age integer
);

CREATE TABLE post_dim
(
  id integer,
  date_key integer,
  score integer,
  view_count integer,
  owner_user_id integer,
  favorite_count integer,
  closed_date timestamp without time zone,
  community_owned_date timestamp without time zone,
  post_type_name character varying(50)
);

CREATE TABLE postHistory_dim
(
  id integer NOT NULL,
  post_History_Type_Name CHARACTER VARYING(50),
  post_Id integer,
  creation_date_key integer
);

CREATE TABLE votes_dim
(
  id integer NOT NULL,
  post_Id integer,
  vote_Type_name CHARACTER VARYING(50)
);


CREATE TABLE comments_dim
(
  id integer NOT NULL,
  post_Id integer,
  score integer,
  creation_Date_key INTEGER
);