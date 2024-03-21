Change Log
==========
* **1.1.1**
    * Added inertia to make parallax and drawer dragging less sensitive and more smooth on screen.
    * Fixed bug where drawers do not snap back to closed or open state when released from dragging.
    * Fixed bug where drawers auto drag to open or closed state when first toggled then dragged.
    * Drawer open and closed states now available via variables `isLeftDrawerOpen` and `isRightDrawerOpen`.

* **1.0.1**
    * Fixed bug where listeners are invoked when any open drawer's content is touched.
    * Drawer open state now saves across configuration changes.

* **1.0.0**
    * Initial release