package com.handgold.pjdc.action;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.Constant;

public class ActionResponse {
	private static ActionResponse mInstance = null;
	
	public ActionResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public static synchronized ActionResponse getInstance(){
		if (mInstance == null) {
			mInstance = new ActionResponse();
		}
		return mInstance;
	}


	public String getActionResDesc(Context context, int res){
		String descriptionString = "";
		switch (res) {
			case Constant.ServerRes.SVR_RES_SUCCESS:
				descriptionString = context.getResources().getString(R.string.operation_success);
				break;
			case Constant.ServerRes.SVR_RES_SERVER_FAULT:
				descriptionString = context.getResources().getString(R.string.server_erro);
				break;
			case Constant.ServerRes.SVR_RES_PARAM_MISS:
				descriptionString = context.getResources().getString(R.string.param_missed);
				break;
			case Constant.ServerRes.SVR_RES_PARAM_FORM_FAULT:
				descriptionString = context.getResources().getString(R.string.param_malform);
				break;
			case Constant.ServerRes.SVR_RES_SMSCODE_INVALID:
				descriptionString = context.getResources().getString(R.string.vericode_invalid);
				break;
			case Constant.ServerRes.SVR_RES_FAULT_UPPER_LIM:
				descriptionString = context.getResources().getString(R.string.server_erro);
				break;
			case Constant.ServerRes.SVR_RES_TOKEN_INVALID:
				descriptionString = context.getResources().getString(R.string.token_invalid);
				break;
			default:
				descriptionString = context.getResources().getString(R.string.unknown_fault);
				Toast.makeText(context,
						descriptionString, Toast.LENGTH_SHORT)
						.show();
				break;
		}
		return descriptionString;
	}

	public void printActionRes(Context context, int res, Throwable error){
		if (context == null){
			return;
		}

		String descriptionString = "";
		switch (res) {
		case Constant.ServerRes.SVR_RES_SUCCESS:
			descriptionString = context.getResources().getString(R.string.operation_success);
			Toast.makeText(context,
					descriptionString, Toast.LENGTH_SHORT)
					.show();
			break;
		case Constant.ServerRes.SVR_RES_SERVER_FAULT:
			descriptionString = context.getResources().getString(R.string.server_erro);
			Toast.makeText(context,
					descriptionString, Toast.LENGTH_SHORT)
					.show();
			break;
		case Constant.ServerRes.SVR_RES_PARAM_MISS:
			descriptionString = context.getResources().getString(R.string.param_missed);
			Toast.makeText(context,
					descriptionString, Toast.LENGTH_SHORT)
					.show();
			break;
		case Constant.ServerRes.SVR_RES_PARAM_FORM_FAULT:
			descriptionString = context.getResources().getString(R.string.param_malform);
			Toast.makeText(context,
					descriptionString, Toast.LENGTH_SHORT)
					.show();
			break;

		case Constant.ServerRes.SVR_RES_SMSCODE_UPPERLIM:
			descriptionString = context.getResources().getString(R.string.vericode_upper_limit);
			Toast.makeText(context,
					descriptionString, Toast.LENGTH_SHORT)
					.show();
			break;
		case Constant.ServerRes.SVR_RES_SMSCODE_INVALID:
			descriptionString = context.getResources().getString(R.string.vericode_invalid);
			Toast.makeText(context,
					descriptionString, Toast.LENGTH_SHORT)
					.show();
			break;
		case Constant.ServerRes.SVR_RES_FAULT_UPPER_LIM:
			descriptionString = context.getResources().getString(R.string.server_erro);
			Toast.makeText(context,
					descriptionString, Toast.LENGTH_SHORT)
					.show();
			break;
		case Constant.ServerRes.SVR_RES_TOKEN_INVALID:
			descriptionString = context.getResources().getString(R.string.token_invalid);
			Toast.makeText(context,
					descriptionString, Toast.LENGTH_SHORT)
					.show();
			break;

		case Constant.ServerRes.SVR_RES_NO_RESPONSE:
			if (error != null) {
				//TODO:判断各种exception
				Log.e("WW", "SVR_RES_NO_RESPONSE exception " + error.toString());
			}
			descriptionString = context.getResources().getString(R.string.no_response);
			Toast.makeText(context,
					descriptionString, Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			descriptionString = context.getResources().getString(R.string.unknown_fault);
			Toast.makeText(context,
					descriptionString, Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}
}
