package com.autentia.example.splitbill.security;

import java.util.Collections;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.reactivestreams.Publisher;

import com.autentia.example.splitbill.persistence.user.UserRepository;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider{

    @Inject
    UserRepository userRepository;

    private boolean authenticate(AuthenticationRequest<?, ?> authenticationRequest) {
        String userId = authenticationRequest.getIdentity().toString();
        String userPwd = authenticationRequest.getSecret().toString();

        return userRepository.existsByUserIdAndPassword(userId, userPwd);
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest,
            AuthenticationRequest<?, ?> authenticationRequest){

        return Flowable.create(emitter -> {
            if ( authenticate(authenticationRequest) ) {
                emitter.onNext(
                        new UserDetails(
                                (String) authenticationRequest.getIdentity(),
                                Collections.EMPTY_SET
                        )
                );
                emitter.onComplete();
            } else {
                emitter.onError(new AuthenticationException(new AuthenticationFailed()));
            }
        }, BackpressureStrategy.ERROR);

    }

}


