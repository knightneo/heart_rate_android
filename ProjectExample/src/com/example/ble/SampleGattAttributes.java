/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ble;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap<String,String>();
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String BODY_SENSOR_LOCATION = "00002a38-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    
    public static String ECG_SERVICE = "000018f0-0000-1000-8000-00805f9b34fb";
    public static String ECG_MEASUREMENT_CHAR = "00002af0-0000-1000-8000-00805f9b34fb";
    //public static String ECG_SERVICE = "000018ff-0000-1000-8000-00805f9b34fb";
    //public static String ECG_MEASUREMENT_CHAR = "00002aff-0000-1000-8000-00805f9b34fb";
    //public static String ECG_WRITE_CHAR = "00002afe-0000-1000-8000-00805f9b34fb";
    
    
    //public static String XK_ECG_SERVICE = "000018f0-0000-1000-8000-00805f9b34fb";
    //public static String XK_ECG_MEASUREMENT = "00002af0-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        attributes.put("0000180f-0000-1000-8000-00805f9b34fb", "Battery Service");
        attributes.put("00001800-0000-1000-8000-00805f9b34fb", "Generic Access");
        attributes.put("00001801-0000-1000-8000-00805f9b34fb", "Generic Attribute");
        attributes.put(ECG_SERVICE, "ECG Service");
        //attributes.put(XK_ECG_SERVICE, "XK_ECG_SERVICE");
        // Sample Characteristics.
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        attributes.put(BODY_SENSOR_LOCATION, "Body Sensor Location");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
        attributes.put("00002a00-0000-1000-8000-00805f9b34fb", "Device Name");
        attributes.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance");
        attributes.put("00002a04-0000-1000-8000-00805f9b34fb", "Peripheral Preferred Connection Parameters");
        attributes.put("00002a05-0000-1000-8000-00805f9b34fb", "Service Changed");
        attributes.put("00002a19-0000-1000-8000-00805f9b34fb", "Battery Level");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
        attributes.put(ECG_MEASUREMENT_CHAR, "ECG Measurement Char");
        //attributes.put(ECG_WRITE_CHAR, "ECG Write Char");
        //attributes.put(XK_ECG_MEASUREMENT, "XK_ECG Measurement");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
