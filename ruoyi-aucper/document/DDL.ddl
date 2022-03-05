
drop table T_PRODUCT_BID_INFO;


/**********************************/
/* テーブル名: 商品入札情報 */
/**********************************/
CREATE TABLE T_PRODUCT_BID_INFO(
        ID                                    BIGINT(10)        NOT NULL AUTO_INCREMENT COMMENT 'ID',
        PRODUCT_CODE                          VARCHAR(30)        NULL            COMMENT '商品コード',
        PRODUCT_TITLE                        VARCHAR(500)        NULL            COMMENT '商品タイトル',
        NOW_PRICE                            INT(20)             DEFAULT '0'     COMMENT '現在価格',
        ONHOLD_PRICE                         INT(20)             DEFAULT '0'     COMMENT '保留価格',
        BID_COUNTDOWN                         INT(10)             DEFAULT '0'     COMMENT '入札カウントダウン',
        BID_START_DATE                         TIMESTAMP            NULL            COMMENT '入札開始日時',
        BID_END_DATE                         TIMESTAMP            NULL            COMMENT '入札終了日時',
        BID_LAST_USER                         VARCHAR(30)         NULL            COMMENT '最後入札者',
        TRUSTEESHIP_USER1                     VARCHAR(30)         NULL            COMMENT '托管ユーザ１',
        TRUSTEESHIP_USER2                     VARCHAR(30)         NULL            COMMENT '托管ユーザ２',
        BID_USER_COUNT                         INT(10)             DEFAULT '0'     COMMENT '入札人数',
        REMAINING_TIME                         TINYINT(11)         DEFAULT '0'     COMMENT '残時間',
        BID_CLOSING_TASK                     CHAR(1)             NULL            COMMENT '入札停止任務',
        BID_PRICE_TASK                         CHAR(1)             NULL            COMMENT '価格任務',
        BID_TASK                             CHAR(1)             NULL            COMMENT '入札任務',
        BID_STATUS                             CHAR(1)             NULL            COMMENT '入札状態',
        TASK_STATUS                         CHAR(1)             NULL            COMMENT '任務状態',
        REMAINING_TIME_UNIT                 VARCHAR(4)             NULL            COMMENT '残時間単位',
        REAL_TIME_STATUS                     CHAR(1)             NULL            COMMENT 'リアルステータス',
        DELETE_FLAG                         TINYINT(1)             DEFAULT '0'     COMMENT '論理削除フラグ',
        UPDATE_COUNT                         INT                 DEFAULT '0'     COMMENT '更新回数',
        CREATE_USER                         VARCHAR(20)         NULL            COMMENT '登録ユーザ',
        CREATE_DATETIME                     TIMESTAMP             NULL             COMMENT '登録日時',
        UPDATE_USER                         VARCHAR(20)         NULL            COMMENT '更新ユーザ',
        UPDATE_DATETIME                     TIMESTAMP             NULL             COMMENT '更新日時',
        PRIMARY KEY (ID)
) COMMENT='商品入札情報' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;





