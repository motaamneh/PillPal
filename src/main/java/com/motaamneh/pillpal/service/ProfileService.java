package com.motaamneh.pillpal.service;

import com.motaamneh.pillpal.io.ProfileRequest;
import com.motaamneh.pillpal.io.ProfileResponse;

public interface ProfileService {
   ProfileResponse createProfile(ProfileRequest request);
}
