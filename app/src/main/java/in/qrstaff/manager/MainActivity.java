package in.qrstaff.manager;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.webkit.*;
import android.widget.*;

public class MainActivity extends Activity {
    private WebView web;
    private ValueCallback<Uri[]> fileCallback;
    private static final int PICK_FILE=1001;

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        String url=getSharedPreferences("qrstaff",0).getString("url","");
        if(url.isEmpty()){
            startActivity(new Intent(this,SettingsActivity.class));
            finish();
            return;
        }
        setup();
        web.loadUrl(url);
    }

    private void setup(){
        web=new WebView(this);
        web.setBackgroundColor(Color.WHITE);
        WebSettings s=web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(web,true);
        web.setWebViewClient(new WebViewClient(){
            @Override public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest r){
                String scheme=r.getUrl().getScheme();
                if("http".equals(scheme)||"https".equals(scheme)) return false;
                try{startActivity(new Intent(Intent.ACTION_VIEW,r.getUrl()));}catch(Exception ignored){}
                return true;
            }
        });
        web.setWebChromeClient(new WebChromeClient(){
            @Override public boolean onShowFileChooser(WebView v,ValueCallback<Uri[]> cb,FileChooserParams p){
                if(fileCallback!=null) fileCallback.onReceiveValue(null);
                fileCallback=cb;
                Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(i,PICK_FILE);
                return true;
            }
        });
        setContentView(web);
    }

    @Override protected void onActivityResult(int req,int result,Intent data){
        super.onActivityResult(req,result,data);
        if(req==PICK_FILE && fileCallback!=null){
            Uri[] r=null;
            if(result==RESULT_OK && data!=null && data.getData()!=null) r=new Uri[]{data.getData()};
            fileCallback.onReceiveValue(r);
            fileCallback=null;
        }
    }

    @Override public void onBackPressed(){
        if(web!=null && web.canGoBack()) web.goBack();
        else new AlertDialog.Builder(this).setTitle("QR Staff").setMessage("Exit the app?")
            .setNegativeButton("Cancel",null).setPositiveButton("Exit",(d,w)->finish()).show();
    }
}
