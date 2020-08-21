package com.lyut.kittenhelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Feline {
    public FelineType Type = FelineType.Invalid;
    public int FollowerId;

    public Feline(int followerId) {
        FollowerId = followerId;
        Type = determineType(followerId);
        if(Type == FelineType.Invalid) {
            throw new IllegalArgumentException("A non-feline follower id was used to try and construct a Feline.");
        }
        log.info("Detected feline of type " + Type.name());
    }

    private FelineType determineType(int followerId) {
        if(followerId >= 1619 && followerId <= 1625) {
            return FelineType.Cat;
        }
        if(followerId >= 1626 && followerId <= 1632) {
            return FelineType.Lazy;
        }
        if(followerId >= 5584 && followerId <= 5590) {
            return FelineType.Wily;
        }
        if(followerId >= 5591 && followerId <= 5597) {
            return FelineType.Kitten;
        }
        if(followerId >= 5598 && followerId <= 5604) {
            return FelineType.Overgrown;
        }
        return FelineType.Invalid;
    }
}
