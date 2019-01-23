package com.blks.https;

public interface HttpUri {

    String LOGIN="/IDS_Host.svc/CAALogon";
    String HEART_BEAT="/IDS_Host.svc/HeartBeat";


    //3.15 询问自动分配任务
    String LISTEN_AUTO_RSC_WO="/RSC_Host.svc/ListenAutoRscWO";
    //3.16 询问手动分配任务
    String LISTEN_MUANUA_RSC_WO="/RSC_Host.svc/ListenManualRscWO";

    //获取用户信息
    String GET_USER_INFO_BY_KEY="/RSC_Host.svc/GetUserInfoByKey";
    //车牌号获取
    String GET_RSC_SPCAR_LIST_BY_SPNO="/RSC_Host.svc/GetRscSPCarListbySPNo";
    //保存救援工与车辆的关系
    String SAVE_VEH_TO_WKR="/RSC_Host.svc/SaveVehToWkr";
    //定位信息回传
    String UPDATE_MY_SITE_ALGORITHM="/RSC_Host.svc/UpdateMySiteAlgorithm";
    //定位信息回传 老接口
    String UPDATE_MY_SITE="/RSC_Host.svc/UpdateMySite";


    //3.25 按页获取工单列表
    String GET_RSC_WO_NSTR_LIST_BY_PAGE="/RSC_Host.svc/GetRscWoMstrListByPage";

    //获取用户状态
    String GET_USER_CURR_STATUS="/IDS_Host.svc/GetUserCurrStatus";
    //修改用户状态
    String UPDATE_CURR_STATUS="/IDS_Host.svc/UpdateCurrStatus";
    //更新工单推送与否状态
    String SET_RSCWO_PUSH_FLAG="/RSC_Host.svc/SetRscWOPushFlag";

    //根据工单号获取工单信息
    String GET_RSC_WOMSTR_BY_WONO="/RSC_Host.svc/GetRscWOMstrByWoNo";
    //回复工单分配状态
    String REPLY_FOR_RSC_WOMSTR="/RSC_Host.svc/ReplyForRscWOMstr";
    //更新工单中救援工的操作状态
    String UPDATE_RSC_WORKER_OPTION="/RSC_Host.svc/UpdateRscWorkerOption";
    //取消/退回工单
    String CANCEL_RSCWO_MSTR="/RSC_Host.svc/CancelRscWoMstr";

    //3.11 获取救援中的工单数量
    String GET_RSC_DOING_CNT="/RSC_Host.svc/GetRscDoingCnt";
    //3.12 获取工单总数
    String GET_RSC_TOTAL_CNT="/RSC_Host.svc/GetRscWOTotalCnt";

    //3.23 保存附件信息
    String SAVE_RSC_PHOTO_INFO="/RSC_Host.svc/SaveRscPhotoInfo";

    // 3.24 保存客户评价
    String SAVE_EVALUATE_INFO="/RSC_Host.svc/SaveEvaluateInfo";

    // 3.4 修改密码
    String RESET_PWD="/IDS_Host.svc/ResetPassword";

    // 3.8 获取APP最新版本信息
    String GET_VERSION_INFO="/RSC_Host.svc/GetSysVersionNoteLatest";

    // 3.14 获取附件列表信息
    String GET_RSC_FILE_LIST="/RSC_Host.svc/GetRscWOFileList";

    //1.7头像信息更新
    String UPDATE_RSC_WORKER_PIC_URL="/RSC_Host.svc/UpdateRscWorkerPicUrl";

    //1.7更新回拨时间
    String UPDATE_CALLBACK_DATE="/RSC_Host.svc/UpdateCallbackDate";
}
