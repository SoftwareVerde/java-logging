package com.softwareverde.util;

import org.junit.Assert;
import org.junit.Test;

public class PackageTests {
    @Test
    public void should_merge_simple_packages() {
        // Setup
        final Package rootPackage = Package.getRootPackage();
        final Package pkg = Package.fromString("com.softwareverde.util");
        Assert.assertEquals("com.softwareverde.util", pkg.toString());

        // Action
        rootPackage.consumePackage(pkg);

        // Assert
        Assert.assertTrue(rootPackage.hasChild("com"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.util"));
        Assert.assertFalse(rootPackage.hasChild("org"));
        Assert.assertFalse(rootPackage.hasChild("com.softwareverde.invalid"));
    }

    @Test
    public void should_merge_packages_with_duplicates() {
        // Setup
        final Package rootPackage = Package.getRootPackage();
        final Package softwareVerdePackage = Package.fromString("com.softwareverde.logging");
        rootPackage.consumePackage(softwareVerdePackage);

        final Package pkg = Package.fromString("com.softwareverde.util");
        Assert.assertEquals("com.softwareverde.util", pkg.toString());

        // Action
        rootPackage.consumePackage(pkg);

        // Assert
        Assert.assertTrue(rootPackage.hasChild("com"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.util"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.logging"));
    }

    @Test
    public void should_merge_packages_with_duplicates_and_existing_children() {
        // Setup
        final Package rootPackage = Package.getRootPackage();
        final Package softwareVerdePackage = Package.fromString("com.softwareverde.logging");
        rootPackage.consumePackage(softwareVerdePackage);

        final Package pkg = Package.fromString("com.softwareverde.constable.list");
        Assert.assertEquals("com.softwareverde.constable.list", pkg.toString());

        // Action
        rootPackage.consumePackage(pkg);

        // Assert
        Assert.assertTrue(rootPackage.hasChild("com"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.constable"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.constable.list"));
        Assert.assertTrue(rootPackage.hasChild("com.softwareverde.logging"));
    }
}
