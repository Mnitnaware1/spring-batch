package com.mastercard.mcbs.pbrc.offline.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineRequest;

@Repository
public interface OfflineRequestRepository extends JpaRepository<OfflineRequest, String> {

	@Query(value = "\r\n"
			+ "   SELECT * FROM bcx_owner.offline_requests u WHERE u.stat_cd = 'REQUEST_RECEIVED' ORDER BY\r\n"
			+ "  u.lst_updt_ts ASC FETCH FIRST ROW ONLY", nativeQuery = true)
	OfflineRequest getOfflineRequest();
}
