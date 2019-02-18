package com.utsav;

import android.app.Application;

import java.util.HashSet;
import java.util.Set;

public class myapplication extends Application {

    public static boolean toblock = true;

    //Stringsets containg app packagenames and app labels

    public static Set<String> flaggedappspackage = new HashSet<String>() {{
        add("com.whatsapp");
    }
    };

    public static Set<String> flaggedappslabel = new HashSet<String>() {{
        add("whatsapp");
    }
    };
}
