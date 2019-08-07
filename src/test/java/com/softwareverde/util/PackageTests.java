package com.softwareverde.util;

import org.junit.Assert;
import org.junit.Test;

public class PackageTests {
    @Test
    public void should_merge_simple_packages() {
        // Setup
        final MutablePackage rootPackage = Package.newRootPackage();
        final MutablePackage pkg = Package.fromString("com.softwareverde.util");
        Assert.assertEquals("com.softwareverde.util", pkg.toString());

        // Action
        rootPackage.mergeInPackage(pkg);

        // Assert
        Assert.assertTrue(rootPackage.hasChild("com"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.util"));
        Assert.assertFalse(rootPackage.hasChild("org"));
        Assert.assertFalse(rootPackage.hasChild("com.softwareverde.invalid"));
        Assert.assertFalse(rootPackage.hasChild("com.softwareverde.constable"));
        Assert.assertFalse(rootPackage.hasChild("com.softwareverde.logging"));
    }

    @Test
    public void should_merge_packages_with_duplicates() {
        // Setup
        final MutablePackage rootPackage = Package.newRootPackage();
        final MutablePackage softwareVerdeLoggingPackage = Package.fromString("com.softwareverde.logging");
        rootPackage.mergeInPackage(softwareVerdeLoggingPackage);

        final MutablePackage pkg = Package.fromString("com.softwareverde.util");
        Assert.assertEquals("com.softwareverde.util", pkg.toString());

        // Action
        rootPackage.mergeInPackage(pkg);

        // Assert
        Assert.assertTrue(rootPackage.hasChild("com"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.util"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.logging"));
        Assert.assertFalse(rootPackage.hasChild("com.softwareverde.constable"));
    }

    @Test
    public void should_merge_packages_with_duplicates_and_existing_children() {
        // Setup
        final MutablePackage rootPackage = Package.newRootPackage();
        final MutablePackage softwareVerdeLoggingPackage = Package.fromString("com.softwareverde.logging");
        rootPackage.mergeInPackage(softwareVerdeLoggingPackage);

        final MutablePackage pkg = Package.fromString("com.softwareverde.constable.list");
        Assert.assertEquals("com.softwareverde.constable.list", pkg.toString());

        // Action
        rootPackage.mergeInPackage(pkg);

        // Assert
        Assert.assertTrue(rootPackage.hasChild("com"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.constable"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.constable.list"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.logging"));
        Assert.assertFalse(rootPackage.hasChild("com.softwareverde.util"));
    }

    @Test
    public void should_merge_merged_packages_with_duplicates_and_existing_children() {
        // Setup
        final MutablePackage rootPackage = Package.newRootPackage();
        final MutablePackage softwareVerdePackage = Package.fromString("com.softwareverde");
        final MutablePackage softwareVerdeLoggingPackage = Package.fromString("com.softwareverde.logging");
        softwareVerdePackage.mergeInPackage(softwareVerdeLoggingPackage);
        rootPackage.mergeInPackage(softwareVerdePackage);

        final MutablePackage pkg = Package.fromString("com.softwareverde.constable");
        final MutablePackage listPkg = Package.fromString("com.softwareverde.constable.list");
        pkg.mergeInPackage(listPkg);
        Assert.assertEquals("com.softwareverde.constable", pkg.toString());
        Assert.assertTrue(pkg.getRoot().hasChild("com.softwareverde.constable.list"));

        // Action
        rootPackage.mergeInPackage(pkg);

        // Assert
        Assert.assertTrue(rootPackage.hasChild("com"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.constable"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.constable.list"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.logging"));
        Assert.assertFalse(rootPackage.hasChild("com.softwareverde.util"));
    }
}
