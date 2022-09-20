
drop table T_PRODUCT_BID_INFO;
drop table T_BID_HIST;


/**********************************/
/* テーブル名: 商品入札情報 */
/**********************************/
CREATE TABLE T_PRODUCT_BID_INFO (
  PRODUCT_CODE 			varchar(30)  		NOT NULL 			COMMENT '商品コード',
  PRODUCT_TITLE 		varchar(500)  		DEFAULT NULL 		COMMENT '商品タイトル',
  CATEGORY 				varchar(3)  		DEFAULT NULL 		COMMENT 'カテゴリ',
  NOW_PRICE 			int(20) 			DEFAULT '0' 		COMMENT '現在価格',
  ONHOLD_PRICE 			int(20) 			DEFAULT '0' 		COMMENT '保留価格',
  BID_START_DATE 		timestamp 			NULL DEFAULT NULL 	COMMENT '入札開始日時',
  BID_END_DATE 			timestamp 			NULL DEFAULT NULL 	COMMENT '入札終了日時',
  BID_LAST_USER 		varchar(30)  		DEFAULT NULL 		COMMENT '最後入札者',
  TRUSTEESHIP_USER1 	varchar(30)  		DEFAULT NULL 		COMMENT '入札ユーザ１',
  TRUSTEESHIP_USER2 	varchar(30)  		DEFAULT NULL 		COMMENT '入札ユーザ２',
  BID_USER_COUNT 		int(10) 			DEFAULT '0' 		COMMENT '入札人数',
  REMAINING_TIME 		int(10) 			DEFAULT NULL 		COMMENT '残時間',
  REMAINING_TIME_UNIT 	varchar(4)  		DEFAULT NULL 		COMMENT '残時間単位',
  BID_STATUS 			char(1)  			DEFAULT NULL 		COMMENT '入札状態　1：出品中、2：終了、3：取消',
  TASK_KIND 			char(1)  			DEFAULT NULL 		COMMENT '任務区分　0：監視、1：自動入札、2：無効',
  REAL_STATUS 			char(1)  			DEFAULT NULL 		COMMENT 'リアルステータス　1：Watching、2：Updating、3：Bidding',
  REMARK 				varchar(100)  		DEFAULT NULL 		COMMENT '備考',
  DELETE_FLAG 			tinyint(1) 			DEFAULT '0' 		COMMENT '論理削除フラグ',
  UPDATE_COUNT 			int(11) 			DEFAULT '0' 		COMMENT '更新回数',
  CREATE_BY 			varchar(20)  		DEFAULT NULL 		COMMENT '登録者',
  CREATE_TIME 			timestamp 			NULL DEFAULT NULL 	COMMENT '登録日時',
  UPDATE_BY 			varchar(20)  		DEFAULT NULL 		COMMENT '更新者',
  UPDATE_TIME 			timestamp 			NULL DEFAULT NULL 	COMMENT '更新日時',
  PRIMARY KEY (PRODUCT_CODE)
) COMMENT='商品入札情報' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

/**********************************/
/* テーブル名: 商品入札履歴 */
/**********************************/
CREATE TABLE T_BID_HIST(
  HIST_DATETIME         TIMESTAMP           NOT NULL            COMMENT '履歴日時',
  PRODUCT_CODE 			varchar(30)  		NOT NULL 			COMMENT '商品コード',
  PRODUCT_TITLE 		varchar(500)  		DEFAULT NULL 		COMMENT '商品タイトル',
  CATEGORY 				varchar(3)  		DEFAULT NULL 		COMMENT 'カテゴリ',
  NOW_PRICE 			int(20) 			DEFAULT '0' 		COMMENT '現在価格',
  ONHOLD_PRICE 			int(20) 			DEFAULT '0' 		COMMENT '保留価格',
  BID_START_DATE 		timestamp 			NULL DEFAULT NULL 	COMMENT '入札開始日時',
  BID_END_DATE 			timestamp 			NULL DEFAULT NULL 	COMMENT '入札終了日時',
  BID_LAST_USER 		varchar(30)  		DEFAULT NULL 		COMMENT '最後入札者',
  TRUSTEESHIP_USER1 	varchar(30)  		DEFAULT NULL 		COMMENT '入札ユーザ１',
  TRUSTEESHIP_USER2 	varchar(30)  		DEFAULT NULL 		COMMENT '入札ユーザ２',
  BID_USER_COUNT 		int(10) 			DEFAULT '0' 		COMMENT '入札人数',
  REMAINING_TIME 		int(10) 			DEFAULT NULL 		COMMENT '残時間',
  REMAINING_TIME_UNIT 	varchar(4)  		DEFAULT NULL 		COMMENT '残時間単位',
  BID_STATUS 			char(1)  			DEFAULT NULL 		COMMENT '入札状態　1：出品中、2：終了、3：取消',
  TASK_KIND 			char(1)  			DEFAULT NULL 		COMMENT '任務区分　0：監視、1：自動入札、2：無効',
  REAL_STATUS 			char(1)  			DEFAULT NULL 		COMMENT 'リアルステータス　1：Watching、2：Updating、3：Bidding',
  REMARK 				varchar(100)  		DEFAULT NULL 		COMMENT '備考',
  DELETE_FLAG 			tinyint(1) 			DEFAULT '0' 		COMMENT '論理削除フラグ',
  UPDATE_COUNT 			int(11) 			DEFAULT '0' 		COMMENT '更新回数',
  CREATE_BY 			varchar(20)  		DEFAULT NULL 		COMMENT '登録者',
  CREATE_TIME 			timestamp 			NULL DEFAULT NULL 	COMMENT '登録日時',
  UPDATE_BY 			varchar(20)  		DEFAULT NULL 		COMMENT '更新者',
  UPDATE_TIME 			timestamp 			NULL DEFAULT NULL 	COMMENT '更新日時',
  PRIMARY KEY (HIST_DATETIME, PRODUCT_CODE)
) COMMENT='商品入札履歴' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

/**********************************/
/* テーブル名: YAHOOアカウント情報 */
/**********************************/
CREATE TABLE `T_YAHOO_ACCOUNT` (
  `ID` 							bigint(20) 		NOT NULL AUTO_INCREMENT 	COMMENT 'ID',
  `YAHOO_ACCOUNT_ID` 			varchar(30)		NOT NULL 					COMMENT 'YAHOOアカウントID',
  `PASSWORD` 					varchar(30) 	NOT NULL 					COMMENT 'パスワード',
  `ANONYMOUS_NAME` 				varchar(30) 	NOT NULL 					COMMENT '匿名',
  `PRIORITY` 					int(2) 			DEFAULT '0' 				COMMENT '優先度',
  `CONVERSATION_COUNT` 			int(1) 			DEFAULT '0' 				COMMENT '会話回数',
  `DELETE_FLAG` 				tinyint(1) 		DEFAULT '0' 				COMMENT '論理削除フラグ',
  `UPDATE_COUNT` 				int(11) 		DEFAULT '0' 				COMMENT '更新回数',
  `CREATE_BY` 					varchar(20) 	DEFAULT NULL 				COMMENT '登録者',
  `CREATE_TIME` 				timestamp 		DEFAULT NULL 				COMMENT '登録日時',
  `UPDATE_BY` 					varchar(20) 	DEFAULT NULL 				COMMENT '更新者',
  `UPDATE_TIME` 				timestamp 		DEFAULT NULL 				COMMENT '更新日時',
  PRIMARY KEY (`ID`),
) COMMENT='YAHOOアカウント情報' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

