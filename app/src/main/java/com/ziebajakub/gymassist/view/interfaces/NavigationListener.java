package com.ziebajakub.gymassist.view.interfaces;

import androidx.fragment.app.Fragment;

public interface NavigationListener {

    void changeFragment(Fragment previous, Fragment next, Boolean addToBackStack);

}