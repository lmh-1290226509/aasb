package com.blks.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blks.adapter.TaskListAdapter;
import com.blks.antrscapp.CameraHelpActivity;
import com.blks.antrscapp.ClientSureActivity;
import com.blks.antrscapp.R;
import com.blks.antrscapp.RecordActivity;
import com.blks.antrscapp.TaskDetailActivity;
import com.blks.antrscapp.WorkOrdersIndexActivity;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.EventOrderModel;
import com.blks.model.WorkOrderModel;
import com.blks.route.Route;
import com.blks.utils.Constants;
import com.blks.utils.LoginUtils;
import com.blks.utils.SharePreferenceUtil;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.baseviewlibrary.adapter.BaseRecyclerAdapter;
import com.ddadai.baseviewlibrary.utils.CustomRecyclerUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerListFragment extends Fragment implements OnClickListener, CustomRecyclerUtil.OnRefreshListener {

    private Context context;
//    private TextView tv_list_help, tv_list_finish;


    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private String text = "已接单,手工派单,预约任务,已出发,已到达,已拍照,拖车出发,拖车到达";

    private TaskListAdapter taskListAdapter;
    private CustomRecyclerUtil cs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        context = this.getActivity();
        findView(view);
        return view;
    }

    private void findView(View view) {
//        tv_list_help = (TextView) view.findViewById(R.id.tv_list_help);
//        tv_list_finish = (TextView) view.findViewById(R.id.tv_list_finish);


        taskListAdapter = new TaskListAdapter(getActivity());
        cs = CustomRecyclerUtil.factory(getContext(), view);
        cs.getRecyclerView().setAdapter(taskListAdapter);

        taskListAdapter.setOnItemClickRecycleListener(new BaseRecyclerAdapter.OnItemClickRecycleListener() {
            @Override
            public void itemClick(int position, Object model) {

                WorkOrderModel.DataListModel dataListModel = (WorkOrderModel.DataListModel) model;

                if ("已接单".equals(dataListModel.RSC_STEP) ||
                        "手工派单".equals(dataListModel.RSC_STEP) ||
                        "预约任务".equals(dataListModel.RSC_STEP)) {

                    modifyUserState(Constants.UserStatus.BUSY);
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                    intent.putExtra(Constants._MODEL, dataListModel);
                    intent.putExtra(Constants._KEY_INT, Constants.OrderStatus.TYPE_1);
                    getActivity().startActivity(intent);

                } else if ("已出发".equals(dataListModel.RSC_STEP)) {

                    modifyUserState(Constants.UserStatus.BUSY);
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                    intent.putExtra(Constants._MODEL, dataListModel);
                    intent.putExtra(Constants._KEY_INT, Constants.OrderStatus.TYPE_2);
                    getActivity().startActivity(intent);

                } else if ("已到达".equals(dataListModel.RSC_STEP) ||
                        "拖车出发".equals(dataListModel.RSC_STEP) ||
                        "拖车到达".equals(dataListModel.RSC_STEP)) {

                    modifyUserState(Constants.UserStatus.READY);
                    Intent intent = new Intent(getContext(), CameraHelpActivity.class);
                    intent.putExtra(Constants._MODEL,  dataListModel);
                    getActivity().startActivity(intent);

                } else if ("已拍照".equals(dataListModel.RSC_STEP)) {

                    modifyUserState(Constants.UserStatus.BUSY);

                    int clienStar = (int) SharePreferenceUtil.get(getContext(), dataListModel.WO_NO+"star", -1);

                    if(clienStar == -1) {
                        Intent intent = new Intent(getContext(), ClientSureActivity.class);
                        intent.putExtra(Constants._WO_NO,  dataListModel.WO_NO);
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), RecordActivity.class);
                        intent.putExtra(Constants._WO_NO, dataListModel.WO_NO);
                        intent.putExtra(Constants._OUT_SOURCE_NO, dataListModel.OUT_SOURCE_NO);
                        intent.putExtra(Constants._KEY_INT, clienStar);
                        getActivity().startActivity(intent);
                    }

                } else {
                    Route.builder(CustomerListFragment.this)
                            .class_(WorkOrdersIndexActivity.class)
                            .putExtra("_WO_NO",dataListModel.WO_NO)
                            .startActivity();

//                    Intent intent = new Intent(getContext(), WorkOrdersIndexActivity.class);
//                    intent.putExtra(Constants._MODEL, dataListModel);
//                    getActivity().startActivity(intent);
                }

            }
        });

//		refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
//		listView = (ListView) view.findViewById(R.id.list_view);
//		adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
//		listView.setAdapter(adapter);

//		ll_help = (LinearLayout) view.findViewById(R.id.ll_help);
//		ll_finish = (LinearLayout) view.findViewById(R.id.ll_finish);
//		ll_help.setOnClickListener(this);
//		ll_finish.setOnClickListener(this);
//
//		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
//			@Override
//			public void onRefresh() {
//				try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				refreshableView.finishRefreshing();
//			}
//		}, 0);

        rg = view.findViewById(R.id.rg);
        rb1 = view.findViewById(R.id.rb1);
        rb2 = view.findViewById(R.id.rb2);

        cs.setOnRefreshListener(this);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        text = "已接单,手工派单,预约任务,已出发,已到达,已拍照,拖车出发,拖车到达";
                        break;
                    case R.id.rb2:
                        text = "已完成,已取消";
                        break;
                }
                cs.headRefresh();
            }
        });
        rb1.setChecked(true);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.ll_help:
//                cleanState();
//                tv_list_help.setTextColor(Color.parseColor("#ffffff"));
//                tv_list_help.setBackgroundColor(Color.parseColor("#4a4aff"));
//                break;
//            case R.id.ll_finish:
//                cleanState();
//                tv_list_finish.setTextColor(Color.parseColor("#ffffff"));
//                tv_list_finish.setBackgroundColor(Color.parseColor("#4a4aff"));
//                break;
//            default:
//                break;
        }
    }

//    private void cleanState() {
//        tv_list_help.setTextColor(Color.parseColor("#4a4aff"));
//        tv_list_help.setBackgroundColor(Color.parseColor("#ffffff"));
//        tv_list_finish.setTextColor(Color.parseColor("#4a4aff"));
//        tv_list_finish.setBackgroundColor(Color.parseColor("#ffffff"));
//    }

    /**
     * 修改用户状态
     * @param state
     */
    protected void modifyUserState(String state) {
        //本地用户状态更新
        LoginUtils.setUserStatus(state);
        //服务器状态更新
        if (LoginUtils.getLoginModel() != null) {
            HttpUtils.get(HttpUri.UPDATE_CURR_STATUS)
                    .dialog(false)
                    .data("usrId", LoginUtils.getLoginModel().USR_ID)
                    .data("status", state)
                    .callBack(new JsonRequestCallBack(getContext()) {
                        @Override
                        public void requestSuccess(String url, JSONObject jsonObject) {

                        }
                    })
                    .request();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cs.headRefresh();
    }

    @Override
    public void onRefresh(final boolean isRefresh, int page, Map<String, Object> params) {

        if (LoginUtils.getLoginModel() == null) {
            return;
        }

        String start = String.valueOf((page-1) * cs.pageSize);

        HttpUtils.get(HttpUri.GET_RSC_WO_NSTR_LIST_BY_PAGE)
                .dialog(true)
                .data("usrId", LoginUtils.getLoginModel().USR_ID)
                .data("usrOrgId", LoginUtils.getLoginModel().ORG_NO)
                .data("search", "")
                .data("timestamp", "")
                .data("start", start)
                .data("length", cs.pageSize+"")
                .data("sColumns", "WO_NO,CREATE_DATE,CAR_NO,RSC_CAR_NO,WO_STATUS")
                .data("iSortCol", "1")
                .data("sSortMode", "desc")
                .data("PARAMS_EQU_RSC_STEP", text)
                .callBack(new JsonRequestCallBack(context) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                        WorkOrderModel model = new Gson().fromJson(jsonObject.toString(), WorkOrderModel.class);
                        if (model.DataList==null||model.DataList.size() < cs.pageSize) {
                            cs.setFoot(false);
                        } else {
                            cs.setFoot(true);
                        }
                        if(isRefresh){
                            taskListAdapter.setModels(model.DataList);
                        }else{
                            taskListAdapter.addModels(model.DataList);
                        }

                        //更新上传点位的工单
                        if (rg.getCheckedRadioButtonId() == R.id.rb1) {

                            List<WorkOrderModel.DataListModel> taskList = taskListAdapter.getModels();
                            if (taskList == null || taskList.size() == 0) {
                                return;
                            }
                            List<EventOrderModel> orderModels = new ArrayList<>();
                            for (WorkOrderModel.DataListModel model1: taskList) {

                                EventOrderModel orderModel = new EventOrderModel("R",
                                        model1.WO_NO, model1.OUT_SOURCE_NO, Constants.OrderAction.ADD);
                                orderModels.add(orderModel);
                            }
                            EventBus.getDefault().post(orderModels);
                        }

                    }

                    @Override
                    public void requestFinish() {
                        super.requestFinish();
                        cs.refreshDone();
                    }
                })
                .request();
    }
}
