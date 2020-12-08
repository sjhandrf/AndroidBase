package com.sjh.common_utils;

import android.content.Intent;

public class IntentUtils {

    public static Intent pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        return intent;
    }


}
