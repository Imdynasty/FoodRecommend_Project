package com.foocmend.services.visit;

import com.foocmend.repositories.SiteVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final SiteVisitRepository repository;

}
