package com.volunnear.services;

import com.volunnear.entitiy.OrganisationRating;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.exceptions.NotFoundException;
import com.volunnear.repositories.OrganisationRatingRepository;
import com.volunnear.repositories.infos.OrganisationInfoRepository;
import com.volunnear.repositories.users.UserRepository;
import com.volunnear.services.interfaces.OrganisationRatingService;
import com.volunnear.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.UUID;

@Service
public class OrganisationRatingServiceImpl implements OrganisationRatingService {

    @Autowired
    private OrganisationRatingRepository organisationRatingRepository;

    @Autowired
    private OrganisationInfoRepository organisationInfoRepository;

    @Autowired
    private UserService userService;

    @Override
    public void addOrUpdateRating(UUID organisationId, int rating, Principal principal) {
        AppUser appUser = userService.findAppUserByUsername(principal.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));
        OrganisationRating organisationRating = organisationRatingRepository
                .findByOrganisationAppUserIdAndUserId(organisationId, appUser.getId())
                .orElseGet(() -> {
                    OrganisationRating newRating = new OrganisationRating();
                    newRating.setOrganisation(organisationInfoRepository.findOrganisationInfoByAppUserId(organisationId)
                            .orElseThrow(() -> new NotFoundException("Organisation not found")));
                    newRating.setUser(appUser);
                    return newRating;
                });

        organisationRating.setRating(rating);
        organisationRatingRepository.save(organisationRating);
    }

    @Override
    @Transactional
    public double getAverageRating(UUID organisationId) {
        OrganisationInfo organisation = organisationInfoRepository.findOrganisationInfoByAppUserId(organisationId)
                .orElseThrow(() -> new NotFoundException("Organisation not found"));
        return organisation.getAverageRating();
    }
}