package com.softwareverde.util;

import java.util.List;

public interface Package {
    static MutablePackage newRootPackage() {
        return MutablePackage.newRootPackage();
    }

    static MutablePackage fromString(final String packageName) {
        MutablePackage rootPackage = new MutablePackage();
        for (final String segmentName : packageName.split("\\.")) {
            final MutablePackage newPackage = new MutablePackage(rootPackage, segmentName);
            newPackage._attachTo(rootPackage);
            rootPackage = newPackage;
        }
        return rootPackage;
    }

    Package getRoot();

    Boolean hasParent();

    Package getParent();

    String getName();

    List<Package> getChildren();

    List<Package> getLeafChildren();

    int getDepth();

    Package getChild(final String name);

    Boolean hasChild(final String name);

    Boolean isRoot();

    @Override
    String toString();

    @Override
    boolean equals(final Object object);

    @Override
    int hashCode();
}