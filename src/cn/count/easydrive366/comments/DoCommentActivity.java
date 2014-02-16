package cn.count.easydrive366.comments;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public class DoCommentActivity extends BaseHttpActivity {
	private String _item_id;
	private String _item_type;
	private RatingBar rb;
	private EditText edtComment;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_docomment);
		this.setupPhoneButtonInVisible();
		this.setupRightButtonWithText("Comment");
		_item_id = getIntent().getStringExtra("id");
		_item_type = getIntent().getStringExtra("type");
		rb =(RatingBar)findViewById(R.id.rating_bar);
		edtComment = (EditText) findViewById(R.id.edt_comment);
		findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doComment();
				
			}
		});
	}
	private void doComment(){
		String comment = edtComment.getText().toString();
		int rating = (int) rb.getRating();
		String url = String.format("comment/edit_comment?userid=%d&id=%s&type=%s&comment=%s&star=%d", AppSettings.userid,_item_id,
				_item_type,comment,rating);
		beginHttp();
		new HttpExecuteGetTask(){

			@Override
			protected void onPostExecute(String result) {
				endHttp();
				processResult(result);
				
			}}.execute(url);
	}
	private void processResult(final String result){
		try{
			JSONObject json = new JSONObject(result);
			if (AppSettings.isSuccessJSON(json)){
				this.setResult(RESULT_OK);
				finish();
			}
		}catch(Exception e){
			log(e);
		}
	}
}
