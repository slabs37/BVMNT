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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "slabs37.batt.volt", "slabs37.batt.volt.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "slabs37.batt.volt", "slabs37.batt.volt.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "slabs37.batt.volt.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
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
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
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
public static String _voltage = "";
public static String _amperage = "";
public static String _vollocation = "";
public static String _amplocation = "";
public static float _wattag = 0f;
public static com.datasteam.b4a.system.superuser.SuShell _su = null;
public anywheresoftware.b4a.objects.LabelWrapper _voltagetext = null;
public anywheresoftware.b4a.objects.ButtonWrapper _setvolpath = null;
public anywheresoftware.b4a.objects.EditTextWrapper _volpath = null;
public anywheresoftware.b4a.objects.EditTextWrapper _amppath = null;
public anywheresoftware.b4a.objects.ButtonWrapper _setamppath = null;
public anywheresoftware.b4a.objects.LabelWrapper _amptext = null;
public anywheresoftware.b4a.objects.LabelWrapper _watttext = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _serviceenable = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _enableamps = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _dumpsys = null;
public anywheresoftware.b4a.objects.EditTextWrapper _batterypath = null;
public anywheresoftware.b4a.objects.ButtonWrapper _batterypathset = null;
public slabs37.batt.volt.adjust _adjust = null;
public slabs37.batt.volt.starter _starter = null;
public slabs37.batt.volt.voltage_notif _voltage_notif = null;
public slabs37.batt.volt.about _about = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (adjust.mostCurrent != null);
vis = vis | (about.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 46;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 47;BA.debugLine="Starter.CheckDivisions";
mostCurrent._starter._checkdivisions /*String*/ ();
 //BA.debugLineNum = 48;BA.debugLine="Activity.LoadLayout(\"1\")";
mostCurrent._activity.LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 50;BA.debugLine="Activity.Title = \"Battery Voltage Mocking Notific";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence("Battery Voltage Mocking Notification Thingy"));
 //BA.debugLineNum = 51;BA.debugLine="Activity.AddMenuItem(\"Adjust\", \"mnuAdjust\")";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("Adjust"),"mnuAdjust");
 //BA.debugLineNum = 52;BA.debugLine="Activity.AddMenuItem(\"About\", \"mnuAbout\")";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("About"),"mnuAbout");
 //BA.debugLineNum = 53;BA.debugLine="Activity.AddMenuItem(\"Exit\", \"mnuExit\")";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("Exit"),"mnuExit");
 //BA.debugLineNum = 56;BA.debugLine="If Starter.kvs.ContainsKey(\"BatteryPath\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("BatteryPath")) { 
 //BA.debugLineNum = 57;BA.debugLine="BatteryPath.Text = Starter.kvs.Get(\"BatteryPath\"";
mostCurrent._batterypath.setText(BA.ObjectToCharSequence(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")));
 }else {
 //BA.debugLineNum = 59;BA.debugLine="Starter.kvs.Put(\"BatteryPath\", \"/sys/class/power";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("BatteryPath",(Object)("/sys/class/power_supply/battery/"));
 //BA.debugLineNum = 60;BA.debugLine="BatteryPath.Text = Starter.kvs.Get(\"BatteryPath\"";
mostCurrent._batterypath.setText(BA.ObjectToCharSequence(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")));
 };
 //BA.debugLineNum = 63;BA.debugLine="If Starter.kvs.ContainsKey(\"VolLocation\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("VolLocation")) { 
 //BA.debugLineNum = 64;BA.debugLine="VolLocation = Starter.kvs.Get(\"VolLocation\")";
_vollocation = BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("VolLocation"));
 //BA.debugLineNum = 65;BA.debugLine="If File.Exists(Starter.kvs.Get(\"BatteryPath\"), V";
if (anywheresoftware.b4a.keywords.Common.File.Exists(BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),_vollocation)) { 
 //BA.debugLineNum = 66;BA.debugLine="GetSetVol";
_getsetvol();
 //BA.debugLineNum = 67;BA.debugLine="VolPath.Text = VolLocation";
mostCurrent._volpath.setText(BA.ObjectToCharSequence(_vollocation));
 };
 };
 //BA.debugLineNum = 71;BA.debugLine="If Starter.kvs.ContainsKey(\"AmpLocation\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("AmpLocation")) { 
 //BA.debugLineNum = 72;BA.debugLine="AmpLocation = Starter.kvs.Get(\"AmpLocation\")";
_amplocation = BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("AmpLocation"));
 //BA.debugLineNum = 73;BA.debugLine="If File.Exists(Starter.kvs.Get(\"BatteryPath\"), A";
if (anywheresoftware.b4a.keywords.Common.File.Exists(BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),_amplocation)) { 
 //BA.debugLineNum = 74;BA.debugLine="GetSetAmp";
_getsetamp();
 //BA.debugLineNum = 75;BA.debugLine="AmpPath.Text = AmpLocation";
mostCurrent._amppath.setText(BA.ObjectToCharSequence(_amplocation));
 };
 };
 //BA.debugLineNum = 78;BA.debugLine="watt";
_watt();
 //BA.debugLineNum = 81;BA.debugLine="If Starter.kvs.ContainsKey(\"MockBatt\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("MockBatt")) { 
 //BA.debugLineNum = 82;BA.debugLine="Dumpsys.Checked = Starter.kvs.Get(\"MockBatt\")";
mostCurrent._dumpsys.setChecked(BA.ObjectToBoolean(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("MockBatt")));
 //BA.debugLineNum = 83;BA.debugLine="Dumpsys_CheckedChange(Starter.kvs.Get(\"MockBatt\"";
_dumpsys_checkedchange(BA.ObjectToBoolean(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("MockBatt")));
 }else {
 //BA.debugLineNum = 85;BA.debugLine="Starter.kvs.Put(\"MockBatt\", False)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("MockBatt",(Object)(anywheresoftware.b4a.keywords.Common.False));
 };
 //BA.debugLineNum = 88;BA.debugLine="If Starter.kvs.ContainsKey(\"ShowAmp\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("ShowAmp")) { 
 //BA.debugLineNum = 89;BA.debugLine="EnableAmps.Checked = Starter.kvs.Get(\"ShowAmp\")";
mostCurrent._enableamps.setChecked(BA.ObjectToBoolean(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("ShowAmp")));
 //BA.debugLineNum = 90;BA.debugLine="EnableAmps_CheckedChange(Starter.kvs.Get(\"ShowAm";
_enableamps_checkedchange(BA.ObjectToBoolean(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("ShowAmp")));
 };
 //BA.debugLineNum = 93;BA.debugLine="If Starter.kvs.ContainsKey(\"ShowNotif\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("ShowNotif")) { 
 //BA.debugLineNum = 94;BA.debugLine="ServiceEnable.Checked = Starter.kvs.Get(\"ShowNot";
mostCurrent._serviceenable.setChecked(BA.ObjectToBoolean(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("ShowNotif")));
 //BA.debugLineNum = 95;BA.debugLine="ServiceEnable_CheckedChange(Starter.kvs.Get(\"Sho";
_serviceenable_checkedchange(BA.ObjectToBoolean(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("ShowNotif")));
 }else {
 //BA.debugLineNum = 97;BA.debugLine="ServiceEnable.Checked = False";
mostCurrent._serviceenable.setChecked(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 98;BA.debugLine="Dumpsys.Visible = False";
mostCurrent._dumpsys.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 100;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 122;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 124;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 102;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 103;BA.debugLine="If Starter.kvs.ContainsKey(\"ShowNotif\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("ShowNotif")) { 
 //BA.debugLineNum = 104;BA.debugLine="ServiceEnable.Checked = Starter.kvs.Get(\"ShowNot";
mostCurrent._serviceenable.setChecked(BA.ObjectToBoolean(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("ShowNotif")));
 //BA.debugLineNum = 105;BA.debugLine="ServiceEnable_CheckedChange(Starter.kvs.Get(\"Sho";
_serviceenable_checkedchange(BA.ObjectToBoolean(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("ShowNotif")));
 //BA.debugLineNum = 106;BA.debugLine="If su.DeviceRooted = True Then";
if (_su.DeviceRooted()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 107;BA.debugLine="Dumpsys.Visible = True";
mostCurrent._dumpsys.setVisible(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 109;BA.debugLine="Dumpsys.Visible = False";
mostCurrent._dumpsys.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 //BA.debugLineNum = 113;BA.debugLine="Starter.kvs.Put(\"ShowNotif\", False)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("ShowNotif",(Object)(anywheresoftware.b4a.keywords.Common.False));
 };
 //BA.debugLineNum = 116;BA.debugLine="If Starter.kvs.ContainsKey(\"MockBatt\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("MockBatt")) { 
 //BA.debugLineNum = 117;BA.debugLine="Dumpsys.Checked = Starter.kvs.Get(\"MockBatt\")";
mostCurrent._dumpsys.setChecked(BA.ObjectToBoolean(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("MockBatt")));
 //BA.debugLineNum = 118;BA.debugLine="Dumpsys_CheckedChange(Starter.kvs.Get(\"MockBatt\"";
_dumpsys_checkedchange(BA.ObjectToBoolean(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("MockBatt")));
 };
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public static String  _amppath_enterpressed() throws Exception{
 //BA.debugLineNum = 175;BA.debugLine="Private Sub AmpPath_EnterPressed";
 //BA.debugLineNum = 176;BA.debugLine="SetAmpPath_Click";
_setamppath_click();
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
return "";
}
public static String  _batterypath_enterpressed() throws Exception{
 //BA.debugLineNum = 239;BA.debugLine="Private Sub BatteryPath_EnterPressed";
 //BA.debugLineNum = 240;BA.debugLine="If Starter.kvs.ContainsKey(\"VolLocation\") = False";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("VolLocation")==anywheresoftware.b4a.keywords.Common.False && mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("AmpLocation")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 241;BA.debugLine="BatteryPathDo";
_batterypathdo();
 }else {
 //BA.debugLineNum = 243;BA.debugLine="If Starter.kvs.ContainsKey(\"VolLocation\") = True";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("VolLocation")==anywheresoftware.b4a.keywords.Common.True && anywheresoftware.b4a.keywords.Common.File.Exists(mostCurrent._batterypath.getText(),mostCurrent._volpath.getText()) && anywheresoftware.b4a.keywords.Common.IsNumber(anywheresoftware.b4a.keywords.Common.File.ReadString(mostCurrent._batterypath.getText(),mostCurrent._volpath.getText()))) { 
 //BA.debugLineNum = 244;BA.debugLine="If Starter.kvs.ContainsKey(\"AmpLocation\") = Tru";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("AmpLocation")==anywheresoftware.b4a.keywords.Common.True && anywheresoftware.b4a.keywords.Common.File.Exists(mostCurrent._batterypath.getText(),mostCurrent._amppath.getText()) && anywheresoftware.b4a.keywords.Common.IsNumber(anywheresoftware.b4a.keywords.Common.File.ReadString(mostCurrent._batterypath.getText(),mostCurrent._amppath.getText()))) { 
 //BA.debugLineNum = 245;BA.debugLine="BatteryPathDo";
_batterypathdo();
 };
 //BA.debugLineNum = 247;BA.debugLine="If Starter.kvs.ContainsKey(\"AmpLocation\") = Fal";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("AmpLocation")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 248;BA.debugLine="BatteryPathDo";
_batterypathdo();
 };
 };
 };
 //BA.debugLineNum = 252;BA.debugLine="End Sub";
return "";
}
public static String  _batterypathdo() throws Exception{
 //BA.debugLineNum = 256;BA.debugLine="Sub BatteryPathDo";
 //BA.debugLineNum = 257;BA.debugLine="If File.IsDirectory(BatteryPath.Text, \"\") = True";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(mostCurrent._batterypath.getText(),"")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 258;BA.debugLine="If BatteryPath.Text.EndsWith(\"/\") = True Then";
if (mostCurrent._batterypath.getText().endsWith("/")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 259;BA.debugLine="Starter.kvs.Put(\"BatteryPath\", BatteryPath.Text";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("BatteryPath",(Object)(mostCurrent._batterypath.getText()));
 //BA.debugLineNum = 260;BA.debugLine="ToastMessageShow(\"Set Battery Folder Path\", Fal";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Set Battery Folder Path"),anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 262;BA.debugLine="If File.IsDirectory(BatteryPath.Text & \"/\", \"\")";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(mostCurrent._batterypath.getText()+"/","")) { 
 //BA.debugLineNum = 263;BA.debugLine="Starter.kvs.Put(\"BatteryPath\", BatteryPath.Tex";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("BatteryPath",(Object)(mostCurrent._batterypath.getText()+"/"));
 //BA.debugLineNum = 264;BA.debugLine="ToastMessageShow(\"Set Battery Folder Path\", Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Set Battery Folder Path"),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 };
 //BA.debugLineNum = 268;BA.debugLine="End Sub";
return "";
}
public static String  _batterypathset_click() throws Exception{
 //BA.debugLineNum = 253;BA.debugLine="Private Sub BatteryPathSet_Click";
 //BA.debugLineNum = 254;BA.debugLine="BatteryPath_EnterPressed";
_batterypath_enterpressed();
 //BA.debugLineNum = 255;BA.debugLine="End Sub";
return "";
}
public static String  _dumpsys_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 227;BA.debugLine="Private Sub Dumpsys_CheckedChange(Checked As Boole";
 //BA.debugLineNum = 228;BA.debugLine="If Checked = True Then";
if (_checked==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 229;BA.debugLine="Starter.kvs.Put(\"MockBatt\", True)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("MockBatt",(Object)(anywheresoftware.b4a.keywords.Common.True));
 };
 //BA.debugLineNum = 231;BA.debugLine="If Checked = False Then";
if (_checked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 232;BA.debugLine="Starter.kvs.Put(\"MockBatt\", False)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("MockBatt",(Object)(anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 233;BA.debugLine="Log(Voltage_notif.su.Execute(\"dumpsys battery re";
anywheresoftware.b4a.keywords.Common.LogImpl("4917510",BA.ObjectToString(mostCurrent._voltage_notif._su /*com.datasteam.b4a.system.superuser.SuShell*/ .Execute(processBA,"dumpsys battery reset")),0);
 //BA.debugLineNum = 234;BA.debugLine="Log(Voltage_notif.su.Execute(\"dumpsys batteryman";
anywheresoftware.b4a.keywords.Common.LogImpl("4917511",BA.ObjectToString(mostCurrent._voltage_notif._su /*com.datasteam.b4a.system.superuser.SuShell*/ .Execute(processBA,"dumpsys batterymanager reset")),0);
 };
 //BA.debugLineNum = 236;BA.debugLine="End Sub";
return "";
}
public static String  _enableamps_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 213;BA.debugLine="Private Sub EnableAmps_CheckedChange(Checked As Bo";
 //BA.debugLineNum = 214;BA.debugLine="If Checked = True Then";
if (_checked==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 215;BA.debugLine="AmpPath.Visible = True";
mostCurrent._amppath.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 216;BA.debugLine="SetAmpPath.Visible = True";
mostCurrent._setamppath.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 217;BA.debugLine="AmpText.Visible = True";
mostCurrent._amptext.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 218;BA.debugLine="Starter.kvs.Put(\"ShowAmp\", True)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("ShowAmp",(Object)(anywheresoftware.b4a.keywords.Common.True));
 };
 //BA.debugLineNum = 220;BA.debugLine="If Checked = False Then";
if (_checked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 221;BA.debugLine="AmpPath.Visible = False";
mostCurrent._amppath.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 222;BA.debugLine="SetAmpPath.Visible = False";
mostCurrent._setamppath.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 223;BA.debugLine="AmpText.Visible = False";
mostCurrent._amptext.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 224;BA.debugLine="Starter.kvs.Put(\"ShowAmp\", False)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("ShowAmp",(Object)(anywheresoftware.b4a.keywords.Common.False));
 };
 //BA.debugLineNum = 226;BA.debugLine="End Sub";
return "";
}
public static void  _getsetamp() throws Exception{
ResumableSub_GetSetAmp rsub = new ResumableSub_GetSetAmp(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GetSetAmp extends BA.ResumableSub {
public ResumableSub_GetSetAmp(slabs37.batt.volt.main parent) {
this.parent = parent;
}
slabs37.batt.volt.main parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 133;BA.debugLine="amperage = File.ReadString(Starter.kvs.Get(\"Batte";
parent._amperage = anywheresoftware.b4a.keywords.Common.File.ReadString(BA.ObjectToString(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),parent._amplocation);
 //BA.debugLineNum = 134;BA.debugLine="AmpText.Text = \"A: \" & amperage";
parent.mostCurrent._amptext.setText(BA.ObjectToCharSequence("A: "+parent._amperage));
 //BA.debugLineNum = 135;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 136;BA.debugLine="GetSetAmp";
_getsetamp();
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _getsetvol() throws Exception{
ResumableSub_GetSetVol rsub = new ResumableSub_GetSetVol(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GetSetVol extends BA.ResumableSub {
public ResumableSub_GetSetVol(slabs37.batt.volt.main parent) {
this.parent = parent;
}
slabs37.batt.volt.main parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 127;BA.debugLine="voltage = File.ReadString(Starter.kvs.Get(\"Batter";
parent._voltage = anywheresoftware.b4a.keywords.Common.File.ReadString(BA.ObjectToString(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),parent._vollocation);
 //BA.debugLineNum = 128;BA.debugLine="VoltageText.Text = \"V: \" & voltage";
parent.mostCurrent._voltagetext.setText(BA.ObjectToCharSequence("V: "+parent._voltage));
 //BA.debugLineNum = 129;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 130;BA.debugLine="GetSetVol";
_getsetvol();
 //BA.debugLineNum = 131;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 29;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 32;BA.debugLine="Private VoltageText As Label";
mostCurrent._voltagetext = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private SetVolPath As Button";
mostCurrent._setvolpath = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private VolPath As EditText";
mostCurrent._volpath = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private AmpPath As EditText";
mostCurrent._amppath = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private SetAmpPath As Button";
mostCurrent._setamppath = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private AmpText As Label";
mostCurrent._amptext = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private WattText As Label";
mostCurrent._watttext = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private ServiceEnable As CheckBox";
mostCurrent._serviceenable = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Public EnableAmps As CheckBox";
mostCurrent._enableamps = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private Dumpsys As CheckBox";
mostCurrent._dumpsys = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private BatteryPath As EditText";
mostCurrent._batterypath = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private BatteryPathSet As Button";
mostCurrent._batterypathset = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public static String  _mnuabout_click() throws Exception{
 //BA.debugLineNum = 273;BA.debugLine="Sub mnuAbout_Click";
 //BA.debugLineNum = 274;BA.debugLine="StartActivity(About)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._about.getObject()));
 //BA.debugLineNum = 275;BA.debugLine="End Sub";
return "";
}
public static String  _mnuadjust_click() throws Exception{
 //BA.debugLineNum = 270;BA.debugLine="Sub mnuAdjust_Click";
 //BA.debugLineNum = 271;BA.debugLine="StartActivity(Adjust)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._adjust.getObject()));
 //BA.debugLineNum = 272;BA.debugLine="End Sub";
return "";
}
public static String  _mnuexit_click() throws Exception{
 //BA.debugLineNum = 276;BA.debugLine="Sub mnuExit_Click";
 //BA.debugLineNum = 277;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 //BA.debugLineNum = 278;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
adjust._process_globals();
starter._process_globals();
voltage_notif._process_globals();
about._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 21;BA.debugLine="Dim voltage As String";
_voltage = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim amperage As String";
_amperage = "";
 //BA.debugLineNum = 23;BA.debugLine="Dim VolLocation As String";
_vollocation = "";
 //BA.debugLineNum = 24;BA.debugLine="Dim AmpLocation As String";
_amplocation = "";
 //BA.debugLineNum = 25;BA.debugLine="Dim wattag As Float";
_wattag = 0f;
 //BA.debugLineNum = 26;BA.debugLine="Dim su As SuShell";
_su = new com.datasteam.b4a.system.superuser.SuShell();
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static String  _serviceenable_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 188;BA.debugLine="Private Sub ServiceEnable_CheckedChange(Checked As";
 //BA.debugLineNum = 189;BA.debugLine="If Starter.kvs.ContainsKey(\"VolLocation\") = True T";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("VolLocation")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 190;BA.debugLine="If Checked = True Then";
if (_checked==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 191;BA.debugLine="StartService(Voltage_notif)";
anywheresoftware.b4a.keywords.Common.StartService(processBA,(Object)(mostCurrent._voltage_notif.getObject()));
 //BA.debugLineNum = 192;BA.debugLine="Voltage_notif.StopNotif = False";
mostCurrent._voltage_notif._stopnotif /*boolean*/  = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 193;BA.debugLine="Voltage_notif.ShowNotif = True";
mostCurrent._voltage_notif._shownotif /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 194;BA.debugLine="Starter.kvs.Put(\"ShowNotif\", True)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("ShowNotif",(Object)(anywheresoftware.b4a.keywords.Common.True));
 //BA.debugLineNum = 195;BA.debugLine="If su.DeviceRooted = True Then";
if (_su.DeviceRooted()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 196;BA.debugLine="Dumpsys.Visible = True";
mostCurrent._dumpsys.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 199;BA.debugLine="If Checked = False Then";
if (_checked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 200;BA.debugLine="StartService(Voltage_notif)";
anywheresoftware.b4a.keywords.Common.StartService(processBA,(Object)(mostCurrent._voltage_notif.getObject()));
 //BA.debugLineNum = 201;BA.debugLine="Voltage_notif.StopNotif = True";
mostCurrent._voltage_notif._stopnotif /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 202;BA.debugLine="Starter.kvs.Put(\"ShowNotif\", False)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("ShowNotif",(Object)(anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 204;BA.debugLine="Dumpsys.Visible = False";
mostCurrent._dumpsys.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 205;BA.debugLine="Dumpsys.Checked = False";
mostCurrent._dumpsys.setChecked(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 206;BA.debugLine="Dumpsys_CheckedChange(False)";
_dumpsys_checkedchange(anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 //BA.debugLineNum = 209;BA.debugLine="ServiceEnable.Checked = False";
mostCurrent._serviceenable.setChecked(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 210;BA.debugLine="Dumpsys.Visible = False";
mostCurrent._dumpsys.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 212;BA.debugLine="End Sub";
return "";
}
public static String  _setamppath_click() throws Exception{
 //BA.debugLineNum = 159;BA.debugLine="Private Sub SetAmpPath_Click";
 //BA.debugLineNum = 160;BA.debugLine="If AmpPath.text.Length = 0 Then";
if (mostCurrent._amppath.getText().length()==0) { 
 //BA.debugLineNum = 161;BA.debugLine="If Starter.kvs.ContainsKey(\"AmpLocation\") = True";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("AmpLocation")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 162;BA.debugLine="Starter.kvs.Remove(\"AmpLocation\")";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._remove("AmpLocation");
 //BA.debugLineNum = 163;BA.debugLine="ToastMessageShow(\"Please restart the app\", Fals";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please restart the app"),anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 //BA.debugLineNum = 166;BA.debugLine="If File.Exists(Starter.kvs.Get(\"BatteryPath\"), A";
if (anywheresoftware.b4a.keywords.Common.File.Exists(BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),mostCurrent._amppath.getText()) && anywheresoftware.b4a.keywords.Common.IsNumber(anywheresoftware.b4a.keywords.Common.File.ReadString(BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),mostCurrent._amppath.getText()))) { 
 //BA.debugLineNum = 167;BA.debugLine="Starter.kvs.Put(\"AmpLocation\", AmpPath.Text)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("AmpLocation",(Object)(mostCurrent._amppath.getText()));
 //BA.debugLineNum = 168;BA.debugLine="AmpLocation = AmpPath.Text";
_amplocation = mostCurrent._amppath.getText();
 //BA.debugLineNum = 169;BA.debugLine="ToastMessageShow(\"Set Ampere File\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Set Ampere File"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 170;BA.debugLine="GetSetAmp";
_getsetamp();
 //BA.debugLineNum = 171;BA.debugLine="watt";
_watt();
 };
 };
 //BA.debugLineNum = 174;BA.debugLine="End Sub";
return "";
}
public static String  _setvolpath_click() throws Exception{
 //BA.debugLineNum = 139;BA.debugLine="Private Sub SetVolPath_Click";
 //BA.debugLineNum = 140;BA.debugLine="If VolPath.Text.Length = 0 Then";
if (mostCurrent._volpath.getText().length()==0) { 
 //BA.debugLineNum = 141;BA.debugLine="If Starter.kvs.ContainsKey(\"VolLocation\") = True";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("VolLocation")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 142;BA.debugLine="Starter.kvs.Remove(\"VolLocation\")";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._remove("VolLocation");
 //BA.debugLineNum = 143;BA.debugLine="ToastMessageShow(\"Please restart the app\", Fals";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please restart the app"),anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 //BA.debugLineNum = 146;BA.debugLine="If File.Exists(Starter.kvs.Get(\"BatteryPath\"), V";
if (anywheresoftware.b4a.keywords.Common.File.Exists(BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),mostCurrent._volpath.getText()) && anywheresoftware.b4a.keywords.Common.IsNumber(anywheresoftware.b4a.keywords.Common.File.ReadString(BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath")),mostCurrent._volpath.getText()))) { 
 //BA.debugLineNum = 147;BA.debugLine="Starter.kvs.Put(\"VolLocation\", VolPath.Text)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("VolLocation",(Object)(mostCurrent._volpath.getText()));
 //BA.debugLineNum = 148;BA.debugLine="VolLocation = VolPath.Text";
_vollocation = mostCurrent._volpath.getText();
 //BA.debugLineNum = 149;BA.debugLine="ToastMessageShow(\"Set Voltage File\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Set Voltage File"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 150;BA.debugLine="GetSetVol";
_getsetvol();
 //BA.debugLineNum = 151;BA.debugLine="watt";
_watt();
 };
 };
 //BA.debugLineNum = 154;BA.debugLine="End Sub";
return "";
}
public static String  _volpath_enterpressed() throws Exception{
 //BA.debugLineNum = 155;BA.debugLine="Private Sub VolPath_EnterPressed";
 //BA.debugLineNum = 156;BA.debugLine="SetVolPath_Click";
_setvolpath_click();
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return "";
}
public static void  _watt() throws Exception{
ResumableSub_watt rsub = new ResumableSub_watt(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_watt extends BA.ResumableSub {
public ResumableSub_watt(slabs37.batt.volt.main parent) {
this.parent = parent;
}
slabs37.batt.volt.main parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 180;BA.debugLine="If Starter.kvs.ContainsKey(\"VolLocation\") And Sta";
if (true) break;

case 1:
//if
this.state = 4;
if (parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("VolLocation") && parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("AmpLocation") && parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("BatteryPath")) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 181;BA.debugLine="wattag=Round(1000*(voltage/Starter.VolDiv)*(ampe";
parent._wattag = (float) (anywheresoftware.b4a.keywords.Common.Round(1000*((double)(Double.parseDouble(parent._voltage))/(double)parent.mostCurrent._starter._voldiv /*int*/ )*((double)(Double.parseDouble(parent._amperage))/(double)parent.mostCurrent._starter._ampdiv /*int*/ )));
 //BA.debugLineNum = 182;BA.debugLine="WattText.Text = \"Usage: \" & wattag & \"mWh\"";
parent.mostCurrent._watttext.setText(BA.ObjectToCharSequence("Usage: "+BA.NumberToString(parent._wattag)+"mWh"));
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 184;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 185;BA.debugLine="watt";
_watt();
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
}
