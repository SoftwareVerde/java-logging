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

    /**
     * Returns the class of the provided Class.
     *  If clazz is an anonymous class, then its parent class name is returned.
     *  i.e. "com.softwareverde.Package$1", where "$1" is the anonymous class, returns as "com.softwareverde.Package".
     *  If the class name has a "$" in its name, it is truncated.
     */
    static String getClassName(final Class<?> clazz) {
        final String className;
        {
            final String canonicalName = clazz.getCanonicalName();
            if (canonicalName != null) {
                className = canonicalName;
            }
            else {
                className = clazz.getName();
            }
        }

        final int anonymousSubclassIndex = className.indexOf("$");
        return (anonymousSubclassIndex < 0 ? className : className.substring(0, anonymousSubclassIndex));
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