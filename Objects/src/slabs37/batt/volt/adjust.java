package slabs37.batt.volt;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class adjust extends Activity implements B4AActivity{
	public static adjust mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "slabs37.batt.volt", "slabs37.batt.volt.adjust");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (adjust).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "slabs37.batt.volt", "slabs37.batt.volt.adjust");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "slabs37.batt.volt.adjust", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (adjust) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (adjust) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return adjust.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (adjust) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (adjust) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            adjust mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (adjust) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static boolean _updatingpreviews = false;
public static boolean _voltset = false;
public static boolean _ampsset = false;
public static com.datasteam.b4a.system.superuser.SuShell _su = null;
public anywheresoftware.b4a.objects.EditTextWrapper _voltdiv = null;
public anywheresoftware.b4a.objects.EditTextWrapper _ampsdiv = null;
public anywheresoftware.b4a.objects.EditTextWrapper _voltdivservice = null;
public anywheresoftware.b4a.objects.LabelWrapper _voltpreview = null;
public anywheresoftware.b4a.objects.ButtonWrapper _back = null;
public anywheresoftware.b4a.objects.LabelWrapper _wattpreview = null;
public anywheresoftware.b4a.objects.ButtonWrapper _voltserviceset = null;
public anywheresoftware.b4a.objects.EditTextWrapper _minvol = null;
public anywheresoftware.b4a.objects.ButtonWrapper _minset = null;
public anywheresoftware.b4a.objects.EditTextWrapper _maxvol = null;
public anywheresoftware.b4a.objects.ButtonWrapper _maxset = null;
public anywheresoftware.b4a.objects.EditTextWrapper _sudumpt = null;
public anywheresoftware.b4a.objects.ButtonWrapper _sudumptest = null;
public anywheresoftware.b4a.objects.ButtonWrapper _sudumpset = null;
public anywheresoftware.b4a.objects.LabelWrapper _explsu = null;
public anywheresoftware.b4a.objects.LabelWrapper _explsuminmax = null;
public anywheresoftware.b4a.objects.LabelWrapper _bgcolor3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _explamp = null;
public anywheresoftware.b4a.objects.LabelWrapper _explvl = null;
public anywheresoftware.b4a.objects.LabelWrapper _bgcolor1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _expl = null;
public slabs37.batt.volt.main _main = null;
public slabs37.batt.volt.starter _starter = null;
public slabs37.batt.volt.voltage_notif _voltage_notif = null;
public slabs37.batt.volt.about _about = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 42;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 43;BA.debugLine="updatingpreviews = False";
_updatingpreviews = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 45;BA.debugLine="Activity.LoadLayout(\"2\")";
mostCurrent._activity.LoadLayout("2",mostCurrent.activityBA);
 //BA.debugLineNum = 47;BA.debugLine="Starter.CheckDivisions";
mostCurrent._starter._checkdivisions /*String*/ ();
 //BA.debugLineNum = 48;BA.debugLine="VoltDiv.Text = Starter.VolDiv";
mostCurrent._voltdiv.setText(BA.ObjectToCharSequence(mostCurrent._starter._voldiv /*int*/ ));
 //BA.debugLineNum = 49;BA.debugLine="AmpsDiv.Text = Starter.AmpDiv";
mostCurrent._ampsdiv.setText(BA.ObjectToCharSequence(mostCurrent._starter._ampdiv /*int*/ ));
 //BA.debugLineNum = 50;BA.debugLine="VoltDivService.Text = Starter.VolServiceDiv";
mostCurrent._voltdivservice.setText(BA.ObjectToCharSequence(mostCurrent._starter._volservicediv /*int*/ ));
 //BA.debugLineNum = 51;BA.debugLine="MaxVol.Text = Starter.HundredVol";
mostCurrent._maxvol.setText(BA.ObjectToCharSequence(mostCurrent._starter._hundredvol /*int*/ ));
 //BA.debugLineNum = 52;BA.debugLine="MinVol.Text = Starter.ZeroVol";
mostCurrent._minvol.setText(BA.ObjectToCharSequence(mostCurrent._starter._zerovol /*int*/ ));
 //BA.debugLineNum = 53;BA.debugLine="SuDumpT.Text = Starter.kvs.Get(\"BatteryServiceNam";
mostCurrent._sudumpt.setText(BA.ObjectToCharSequence(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryServiceName")));
 //BA.debugLineNum = 56;BA.debugLine="If Starter.kvs.ContainsKey(\"VolLocation\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("VolLocation")) { 
 //BA.debugLineNum = 57;BA.debugLine="If File.Exists(Starter.kvs.Get(\"BatteryPath\"), S";
if (anywheresoftware.b4a.keywords.Common.File.Exists(BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("VolLocation")))) { 
 //BA.debugLineNum = 58;BA.debugLine="VoltSet = True";
_voltset = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 60;BA.debugLine="VoltSet = False";
_voltset = anywheresoftware.b4a.keywords.Common.False;
 };
 }else {
 //BA.debugLineNum = 63;BA.debugLine="VoltSet = False";
_voltset = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 66;BA.debugLine="If Starter.kvs.ContainsKey(\"AmpLocation\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("AmpLocation")) { 
 //BA.debugLineNum = 67;BA.debugLine="If File.Exists(Starter.kvs.Get(\"BatteryPath\"), S";
if (anywheresoftware.b4a.keywords.Common.File.Exists(BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("AmpLocation")))) { 
 //BA.debugLineNum = 68;BA.debugLine="AmpsSet = True";
_ampsset = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 70;BA.debugLine="AmpsSet = False";
_ampsset = anywheresoftware.b4a.keywords.Common.False;
 };
 }else {
 //BA.debugLineNum = 73;BA.debugLine="AmpsSet = False";
_ampsset = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 76;BA.debugLine="If updatingpreviews = False Then";
if (_updatingpreviews==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 77;BA.debugLine="Preview_Update";
_preview_update();
 };
 //BA.debugLineNum = 81;BA.debugLine="If su.DeviceRooted = True Then";
if (_su.DeviceRooted()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 82;BA.debugLine="ExplSu.Visible = True";
mostCurrent._explsu.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 83;BA.debugLine="SuDumpT.Visible = True";
mostCurrent._sudumpt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 84;BA.debugLine="SuDumpTest.Visible = True";
mostCurrent._sudumptest.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 85;BA.debugLine="SuDumpSet.Visible = True";
mostCurrent._sudumpset.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 86;BA.debugLine="ExplSuMinMax.Visible = True";
mostCurrent._explsuminmax.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 87;BA.debugLine="MinVol.Visible = True";
mostCurrent._minvol.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 88;BA.debugLine="MinSet.Visible = True";
mostCurrent._minset.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 89;BA.debugLine="MaxSet.Visible = True";
mostCurrent._maxset.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 90;BA.debugLine="MaxVol.Visible = True";
mostCurrent._maxvol.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 91;BA.debugLine="bgcolor3.Visible = True";
mostCurrent._bgcolor3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 95;BA.debugLine="If Starter.kvs.ContainsKey(\"VolLocation\") And Sta";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("VolLocation") && mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("AmpLocation") && mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("BatteryPath")) { 
 //BA.debugLineNum = 96;BA.debugLine="Expl.Visible = True";
mostCurrent._expl.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 97;BA.debugLine="Explvl.Visible = True";
mostCurrent._explvl.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 98;BA.debugLine="Explamp.Visible = True";
mostCurrent._explamp.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 99;BA.debugLine="AmpsDiv.Visible = True";
mostCurrent._ampsdiv.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 100;BA.debugLine="VoltDiv.Visible = True";
mostCurrent._voltdiv.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 101;BA.debugLine="WattPreview.Visible = True";
mostCurrent._wattpreview.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 102;BA.debugLine="bgcolor1.Visible = True";
mostCurrent._bgcolor1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 104;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 110;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 111;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 106;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return "";
}
public static String  _ampsdiv_enterpressed() throws Exception{
 //BA.debugLineNum = 131;BA.debugLine="Private Sub AmpsDiv_EnterPressed";
 //BA.debugLineNum = 132;BA.debugLine="Starter.AmpDiv = AmpsDiv.Text";
mostCurrent._starter._ampdiv /*int*/  = (int)(Double.parseDouble(mostCurrent._ampsdiv.getText()));
 //BA.debugLineNum = 133;BA.debugLine="Starter.kvs.Put(\"AmpDiv\", Starter.AmpDiv)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("AmpDiv",(Object)(mostCurrent._starter._ampdiv /*int*/ ));
 //BA.debugLineNum = 134;BA.debugLine="ToastMessageShow(\"Set Ampere Division\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Set Ampere Division"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return "";
}
public static String  _back_click() throws Exception{
 //BA.debugLineNum = 143;BA.debugLine="Private Sub Back_Click";
 //BA.debugLineNum = 144;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 145;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 19;BA.debugLine="Private VoltDiv As EditText";
mostCurrent._voltdiv = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private AmpsDiv As EditText";
mostCurrent._ampsdiv = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private VoltDivService As EditText";
mostCurrent._voltdivservice = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private VoltPreview As Label";
mostCurrent._voltpreview = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private Back As Button";
mostCurrent._back = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private WattPreview As Label";
mostCurrent._wattpreview = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private VoltServiceSet As Button";
mostCurrent._voltserviceset = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private MinVol As EditText";
mostCurrent._minvol = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private MinSet As Button";
mostCurrent._minset = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private MaxVol As EditText";
mostCurrent._maxvol = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private MaxSet As Button";
mostCurrent._maxset = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private SuDumpT As EditText";
mostCurrent._sudumpt = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private SuDumpTest As Button";
mostCurrent._sudumptest = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private SuDumpSet As Button";
mostCurrent._sudumpset = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private ExplSu As Label";
mostCurrent._explsu = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private ExplSuMinMax As Label";
mostCurrent._explsuminmax = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private bgcolor3 As Label";
mostCurrent._bgcolor3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private Explamp As Label";
mostCurrent._explamp = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private Explvl As Label";
mostCurrent._explvl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private bgcolor1 As Label";
mostCurrent._bgcolor1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private Expl As Label";
mostCurrent._expl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}
public static String  _maxset_click() throws Exception{
 //BA.debugLineNum = 167;BA.debugLine="Private Sub MaxSet_Click";
 //BA.debugLineNum = 168;BA.debugLine="MaxVol_EnterPressed";
_maxvol_enterpressed();
 //BA.debugLineNum = 169;BA.debugLine="End Sub";
return "";
}
public static String  _maxvol_enterpressed() throws Exception{
 //BA.debugLineNum = 162;BA.debugLine="Private Sub MaxVol_EnterPressed";
 //BA.debugLineNum = 163;BA.debugLine="Starter.HundredVol = MaxVol.Text";
mostCurrent._starter._hundredvol /*int*/  = (int)(Double.parseDouble(mostCurrent._maxvol.getText()));
 //BA.debugLineNum = 164;BA.debugLine="Starter.kvs.Put(\"HundredVol\", MaxVol.Text)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("HundredVol",(Object)(mostCurrent._maxvol.getText()));
 //BA.debugLineNum = 165;BA.debugLine="ToastMessageShow(\"Set 100%\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Set 100%"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 166;BA.debugLine="End Sub";
return "";
}
public static String  _minset_click() throws Exception{
 //BA.debugLineNum = 158;BA.debugLine="Private Sub MinSet_Click";
 //BA.debugLineNum = 159;BA.debugLine="MinVol_EnterPressed";
_minvol_enterpressed();
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}
public static String  _minvol_enterpressed() throws Exception{
 //BA.debugLineNum = 153;BA.debugLine="Private Sub MinVol_EnterPressed";
 //BA.debugLineNum = 154;BA.debugLine="Starter.ZeroVol = MinVol.Text";
mostCurrent._starter._zerovol /*int*/  = (int)(Double.parseDouble(mostCurrent._minvol.getText()));
 //BA.debugLineNum = 155;BA.debugLine="Starter.kvs.Put(\"ZeroVol\", MinVol.Text)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("ZeroVol",(Object)(mostCurrent._minvol.getText()));
 //BA.debugLineNum = 156;BA.debugLine="ToastMessageShow(\"Set 0%\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Set 0%"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return "";
}
public static void  _preview_update() throws Exception{
ResumableSub_Preview_Update rsub = new ResumableSub_Preview_Update(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Preview_Update extends BA.ResumableSub {
public ResumableSub_Preview_Update(slabs37.batt.volt.adjust parent) {
this.parent = parent;
}
slabs37.batt.volt.adjust parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 114;BA.debugLine="updatingpreviews = True";
parent._updatingpreviews = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 115;BA.debugLine="If VoltSet = True Then";
if (true) break;

case 1:
//if
this.state = 4;
if (parent._voltset==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 116;BA.debugLine="VoltPreview.Text = (File.ReadString(Starter.kvs.";
parent.mostCurrent._voltpreview.setText(BA.ObjectToCharSequence(((double)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.File.ReadString(BA.ObjectToString(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),BA.ObjectToString(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("VolLocation")))))/(double)parent.mostCurrent._starter._volservicediv /*int*/ )));
 if (true) break;
;
 //BA.debugLineNum = 118;BA.debugLine="If VoltSet = True And AmpsSet = True Then";

case 4:
//if
this.state = 7;
if (parent._voltset==anywheresoftware.b4a.keywords.Common.True && parent._ampsset==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 119;BA.debugLine="WattPreview.Text=(File.ReadString(Starter.kvs.Ge";
parent.mostCurrent._wattpreview.setText(BA.ObjectToCharSequence(((double)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.File.ReadString(BA.ObjectToString(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),BA.ObjectToString(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("VolLocation")))))/(double)parent.mostCurrent._starter._voldiv /*int*/ )*((double)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.File.ReadString(BA.ObjectToString(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),BA.ObjectToString(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("AmpLocation")))))/(double)parent.mostCurrent._starter._ampdiv /*int*/ )));
 if (true) break;

case 7:
//C
this.state = -1;
;
 //BA.debugLineNum = 121;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 8;
return;
case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 122;BA.debugLine="Preview_Update";
_preview_update();
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim updatingpreviews As Boolean";
_updatingpreviews = false;
 //BA.debugLineNum = 10;BA.debugLine="Dim VoltSet As Boolean";
_voltset = false;
 //BA.debugLineNum = 11;BA.debugLine="Dim AmpsSet As Boolean";
_ampsset = false;
 //BA.debugLineNum = 12;BA.debugLine="Dim su As SuShell";
_su = new com.datasteam.b4a.system.superuser.SuShell();
 //BA.debugLineNum = 13;BA.debugLine="End Sub";
return "";
}
public static String  _sudumpset_click() throws Exception{
 //BA.debugLineNum = 179;BA.debugLine="Private Sub SuDumpSet_Click";
 //BA.debugLineNum = 180;BA.debugLine="Starter.kvs.Put(\"BatteryServiceName\", SuDumpT.Tex";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("BatteryServiceName",(Object)(mostCurrent._sudumpt.getText()));
 //BA.debugLineNum = 181;BA.debugLine="End Sub";
return "";
}
public static void  _sudumptest_click() throws Exception{
ResumableSub_SuDumpTest_Click rsub = new ResumableSub_SuDumpTest_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_SuDumpTest_Click extends BA.ResumableSub {
public ResumableSub_SuDumpTest_Click(slabs37.batt.volt.adjust parent) {
this.parent = parent;
}
slabs37.batt.volt.adjust parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 172;BA.debugLine="ToastMessageShow(\"setting battery to 1%\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("setting battery to 1%"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 173;BA.debugLine="Log(su.Execute(\"dumpsys \" & SuDumpT.Text & \" set";
anywheresoftware.b4a.keywords.Common.LogImpl("42359298",BA.ObjectToString(parent._su.Execute(processBA,"dumpsys "+parent.mostCurrent._sudumpt.getText()+" set level 1")),0);
 //BA.debugLineNum = 174;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 175;BA.debugLine="ToastMessageShow(\"resetting battery\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("resetting battery"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 176;BA.debugLine="Log(su.Execute(\"dumpsys \" & SuDumpT.Text & \" rese";
anywheresoftware.b4a.keywords.Common.LogImpl("42359301",BA.ObjectToString(parent._su.Execute(processBA,"dumpsys "+parent.mostCurrent._sudumpt.getText()+" reset")),0);
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _voltdiv_enterpressed() throws Exception{
 //BA.debugLineNum = 125;BA.debugLine="Private Sub VoltDiv_EnterPressed";
 //BA.debugLineNum = 126;BA.debugLine="Starter.VolDiv = VoltDiv.Text";
mostCurrent._starter._voldiv /*int*/  = (int)(Double.parseDouble(mostCurrent._voltdiv.getText()));
 //BA.debugLineNum = 127;BA.debugLine="Starter.kvs.Put(\"VolDiv\", Starter.VolDiv)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("VolDiv",(Object)(mostCurrent._starter._voldiv /*int*/ ));
 //BA.debugLineNum = 128;BA.debugLine="ToastMessageShow(\"Set Voltage Division\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Set Voltage Division"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 129;BA.debugLine="End Sub";
return "";
}
public static String  _voltdivservice_enterpressed() throws Exception{
 //BA.debugLineNum = 137;BA.debugLine="Private Sub VoltDivService_EnterPressed";
 //BA.debugLineNum = 138;BA.debugLine="Starter.VolServiceDiv = VoltDivService.Text";
mostCurrent._starter._volservicediv /*int*/  = (int)(Double.parseDouble(mostCurrent._voltdivservice.getText()));
 //BA.debugLineNum = 139;BA.debugLine="Starter.kvs.Put(\"VolServiceDiv\", Starter.VolServi";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("VolServiceDiv",(Object)(mostCurrent._starter._volservicediv /*int*/ ));
 //BA.debugLineNum = 140;BA.debugLine="ToastMessageShow(\"Set Voltage Division\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Set Voltage Division"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return "";
}
public static String  _voltserviceset_click() throws Exception{
 //BA.debugLineNum = 147;BA.debugLine="Private Sub VoltServiceSet_Click";
 //BA.debugLineNum = 148;BA.debugLine="Starter.VolServiceDiv = VoltDivService.Text";
mostCurrent._starter._volservicediv /*int*/  = (int)(Double.parseDouble(mostCurrent._voltdivservice.getText()));
 //BA.debugLineNum = 149;BA.debugLine="Starter.kvs.Put(\"VolServiceDiv\", Starter.VolServi";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("VolServiceDiv",(Object)(mostCurrent._starter._volservicediv /*int*/ ));
 //BA.debugLineNum = 150;BA.debugLine="ToastMessageShow(\"Set Voltage Division\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Set Voltage Division"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
}
