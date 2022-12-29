package com.kob.backend.service.record;

import com.alibaba.fastjson.JSONObject;

public interface GetRecordListService {
    // The record list should be paginated in the backend instead of the frontend
    // Otherwise it will increase the communication pressure
    // Set 10 records per page
    JSONObject getList(Integer page);
}
