package com.example.vdha3.secureval;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.rootbeer.RootBeer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

public class MainActivity extends AppCompatActivity {

    TextView name;
    Button StringBuffer;
    StringBuffer analysis = new StringBuffer();
    String[] locks = {"WeakLock","MediumLock","StrongLock"};

    String[] listOfGoals = {"LatestOS","Bootloader","Root","De_eloperOptions","TooManyPermissions","Ad_anced","Risky"};
    String[] listOfGoalsUser =
            {"Latest version of OS","Bootloader unlocked","Device rooted","Developer Options Enabled","Too many permissions","Advanced options used","Risky usage"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        StringBuffer = findViewById(R.id.StringBuffer);

        setSupportActionBar(toolbar);
        name = findViewById(R.id.vidha);
        String rules_facts = "";
        File rules = new File("KB.txt");
        BufferedReader br;
        try {
            br = new BufferedReader(
                    new InputStreamReader(getAssets().open("KB.txt")));
            //s = new Scanner(rules);
            String line;
            while ((line=br.readLine())!=null) {
                rules_facts = rules_facts + line + "\n";

            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        String file_read = "File read "+rules_facts+"\n";
        //OS version
        int latestOs = Build.VERSION.SDK_INT;
        //int os_score = 0;       //METRIC
        String os;
        switch (latestOs) {
            case 23:

                rules_facts+="SDK23\n"; break;
            case 24:
                rules_facts+="SDK24\n"; break;
            case 25:
                rules_facts+="SDK25\n"; break;
                //Toast.makeText(this, "Noughat", Toast.LENGTH_SHORT).show();
            case 26:
                rules_facts+="SDK26\n"; break;
            case 27:
                rules_facts+="SDK27\n"; break;
                //Toast.makeText(this, "Oreo", Toast.LENGTH_SHORT).show();
            default:
                rules_facts+="SDKold\n";break;
                //Toast.makeText(this, "You have an old phone", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "" + latestOs, Toast.LENGTH_SHORT).show();


        //bootloader info
        String model = Build.BRAND;
        String boot_loader = Build.BOOTLOADER;
        //Toast.makeText(this, "bhooth"+boot_loader, Toast.LENGTH_SHORT).show();

        rules_facts+="BootloaderLocked\n";
        //if ()
        //developer options
        //Boolean devTrueFalse = false;       //METRIC
        int devops = Settings.Secure.getInt(this.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0);
        if (devops != 0) {
            //devTrueFalse = true;
            //Toast.makeText(this, devops + " I am a Dev", Toast.LENGTH_SHORT).show();
            rules_facts += "De_eloperOptions\n";
        }
        else
            rules_facts += "De_eloperOptionsNotEnabled\n";


        //rooted or not?
        RootBeer rootBeer = new RootBeer(getApplicationContext());
        //Boolean rootOrNot = false;
        if (rootBeer.isRooted()) {
            //we found indication of root
            //rootOrNot = true;
            //Toast.makeText(this, "Yes Bro", Toast.LENGTH_SHORT).show();
            rules_facts += "Root\n";
        } else {
            //we didn't find indication of root
            //Toast.makeText(this, "No Bro", Toast.LENGTH_SHORT).show();
            rules_facts+= "NoRoot\n";
        }

        //locktype
        LockType locky = new LockType();
        int lockNo = locky.getCurrent(getContentResolver(), getApplicationContext());

        //Boolean lockOrNot = true;
        //Toast.makeText(this, "Lock no" + lockNo, Toast.LENGTH_SHORT).show();
        switch (lockNo) {
            case LockType.FACE_WITH_PIN:
                //name.setText("PIN");
                rules_facts += "FaceWithPin\n";
                break;
            case LockType.FACE_WITH_SOMETHING_ELSE:
                //name.setText("Something");
                rules_facts += "FaceWithElse\n";
                break;
            case LockType.PASSWORD_FINGERPRINT:
                //name.setText("Fingerprint");
                rules_facts += "Fingerprint\n";
                break;
            case LockType.PATTERN:
                rules_facts += "Pattern\n";
                break;
            case LockType.PASSWORD_ALPHABETIC:
                rules_facts += "PasswordAlpha\n";
                break;
            case LockType.PASSWORD_ALPHANUMERIC:
                rules_facts += "PasswordAlphaNum\n";
                break;
            case LockType.PIN:
                rules_facts += "PIN\n";
                break;
            default:
                //name.setText("No lock");
                //lockOrNot = false;
                rules_facts +="NoLock\n";
                break;
        }

        //applications
        PackageManager pack = getPackageManager();
        int appCount = 0;
        StringBuffer app_perm = new StringBuffer();
        String[] req_perm;
        PermissionInfo[] perm_info;
        int no_perm;
        float ratio, ratio_sum=0;

        List<ApplicationInfo> packages = pack.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packu : packages) {
            //Log.d("Installed Apps", packu.packageName + packu.sourceDir);
            appCount++;
            ratio = 1;
            //Toast.makeText(this, "Permissions "+packu.permission, Toast.LENGTH_SHORT).show();
            try {
                PackageInfo packageInfo = pack.getPackageInfo(packu.packageName, PackageManager.GET_PERMISSIONS);
                app_perm.append(packageInfo.packageName + ":\n");
                req_perm = packageInfo.requestedPermissions;
                perm_info = packageInfo.permissions;
                if (req_perm!=null) {
                    app_perm.append("Requested: "+req_perm.length+"\n");
                    //for (int i=0; i<req_perm.length; i++){
                    //    app_perm.append(req_perm[i]+"\t");
                    //}
                }
                else
                    ratio = 0;
                if (perm_info!=null) {
                    app_perm.append("Permissions " + perm_info.length + "\n");
                    //for (int i=0; i<perm_info.length; i++){
                    //    app_perm.append(perm_info[i]+"\t");
                    //}
                }
                else
                    ratio = 0;
                if (ratio!=0)
                    ratio = perm_info.length/req_perm.length;
                ratio_sum+=ratio;
            } catch (Exception e) {
                Toast.makeText(this, "Permission Exception", Toast.LENGTH_SHORT).show();
            }

        }
        float avg_ratio = ratio_sum/appCount;
        //name.setText("Ratio "+avg_ratio);
        if (avg_ratio > 0.25)
            rules_facts += "MoreThan25";
       /* BufferedWriter out = null;
        try
        {
            FileWriter fstream = new FileWriter("new_rules_facts.txt", true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write(rules_facts);
        }
        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
        }*/
        int score = 0;
        //StringBuffer analysis = new StringBuffer();
        String alg = "forward";
        //updates the score according to the type of lock;

        if (Entail.main_entail(alg, rules_facts, listOfGoals[0])) {
            analysis.append(listOfGoalsUser[0]=": Yes!\n");
            score += 5;
        } else {
            score -= 1;
            analysis.append(listOfGoalsUser[0]+": No!\n");
        }
        for (int i=0; i<locks.length; i++){
            if(Entail.main_entail(alg,rules_facts,locks[i])){

                score+=(i*5);
                analysis.append("Lock level: "+locks[i]+"\n");
                break;
            }
        }
        for (int i=1; i<listOfGoals.length; i++) {
            if (Entail.main_entail(alg, rules_facts, listOfGoals[i])) {
                analysis.append(listOfGoalsUser[i]=": Yes!\n");
                score -= 1;
            } else {
                score += 5;
                analysis.append(listOfGoalsUser[i]+": No!\n");
            }
        }
        float score_pc = (score/((listOfGoals.length*5)+10))*100;
        analysis.append("\nTotal Score out of 100: "+score_pc);
        if (score_pc<40)
            analysis.append("\nOverall performance: Needs major improvement!");
        else if (score_pc<75)
            analysis.append("\nOverall performance: Not bad. But you could do better!");
        else
            analysis.append("\nOverall performance: Pretty great! Keep it up :)");
        name.setText(rules_facts+"\n"+analysis);

        StringBuffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AnalysisActivity.class);
                intent.putExtra("analysis",analysis.toString());
                //intent.putExtra("analysis",analysis.toString());`

                startActivity(intent);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
