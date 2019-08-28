package com.softwareverde.logging;

import com.softwareverde.util.MutablePackage;
import com.softwareverde.util.Package;

public class PackageLevel extends MutablePackage {
    public static PackageLevel newRootPackage() {
        return new PackageLevel(null, null);
    }

    public static PackageLevel fromClass(final Class<?> clazz, final LogLevel level) {
        final String classPackage = Package.getClassName(clazz);
        return PackageLevel.fromString(classPackage, level);
    }

    public static PackageLevel fromString(final String packageName, final LogLevel logLevel) {
        PackageLevel rootPackage = new PackageLevel();
        for (final String segmentName : packageName.split("\\.")) {
            final PackageLevel newPackage = new PackageLevel(rootPackage, segmentName);
            newPackage._attachTo(rootPackage);
            rootPackage = newPackage;
        }

        rootPackage.setLogLevel(logLevel); // Only set the LogLevel for the last segment.
        return rootPackage;
    }

    protected static LogLevel getLogLevelOrNull(final Package pkg) {
        if (! (pkg instanceof PackageLevel)) { return null; }
        return ((PackageLevel) pkg).getLogLevel();
    }

    protected LogLevel _logLevel;

    protected PackageLevel() {
        _logLevel = null;
    }

    protected PackageLevel(final MutablePackage parent, final String name) {
        super(parent, name);
        _logLevel = null;
    }

    protected PackageLevel(final PackageLevel parent, final String name) {
        super(parent, name);
        _logLevel = (parent != null ? parent.getLogLevel() : null);
    }

    public void setLogLevel(final LogLevel logLevel) {
        _logLevel = logLevel;
    }

    public LogLevel getLogLevel() {
        return _logLevel;
    }

    @Override
    public void clear() {
        super.clear();
        _logLevel = null;
    }

    /**
     * Returns the LogLevel of the specified package name, relative to (i.e. a descendant of) this node.
     *  If the end node does not have a LogLevel set, then the closest LogLevel is returned, including an ancestor of this node.
     *  If no LogLevels are set, then null is returned.
     */
    public LogLevel getLogLevel(final String relativePackageName) {
        LogLevel logLevel = null;
        Package pkg = this;

        // Check descendants for a LogLevel...
        for (final String segmentName : relativePackageName.split("\\.")) {
            pkg = pkg.getChild(segmentName);
            if (pkg == null) { break; }

            final LogLevel newLogLevel = PackageLevel.getLogLevelOrNull(pkg);
            if (newLogLevel != null) {
                logLevel = newLogLevel;
            }
        }

        if (logLevel == null) {
            // Check ancestors for a LogLevel...
            pkg = this;
            while ( (pkg.hasParent()) && (logLevel == null) ) {
                final LogLevel newLogLevel = PackageLevel.getLogLevelOrNull(pkg);
                if (newLogLevel != null) {
                    logLevel = newLogLevel;
                }
            }
        }

        return logLevel;
    }

    @Override
    protected void _attachTo(final MutablePackage anchorPackage) {
        if ( (_logLevel != null) && (anchorPackage.hasChild(_name)) ) {
            final Package anchorPackageChild = anchorPackage.getChild(_name);
            if (anchorPackageChild instanceof PackageLevel) {
                ((PackageLevel) anchorPackageChild).setLogLevel(_logLevel);
            }
        }

        super._attachTo(anchorPackage);
    }

    @Override
    public void mergeInPackage(final MutablePackage pkg) {
        final Package thisRoot = _getRoot();
        final Package pkgRoot = pkg.getRoot();

        if ( (thisRoot instanceof PackageLevel) && (pkgRoot instanceof PackageLevel) ) {
            final PackageLevel thisRootPackageLevel = (PackageLevel) thisRoot;
            final PackageLevel pkgRootPackageLevel = (PackageLevel) pkgRoot;

            if (thisRootPackageLevel.getLogLevel() == null) {
                final LogLevel logLevel = pkgRootPackageLevel.getLogLevel();
                thisRootPackageLevel.setLogLevel(logLevel);
            }
        }

        super.mergeInPackage(pkg);
    }
}
