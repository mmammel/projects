KPL/FK

MSL Frames Kernel
========================================================================

   This frame kernel contains complete set of frame definitions for the
   MSL including definitions for the MSL cruise, descent, and rover
   frames, local level, topocentric and surface-fixed frames, appendage 
   frames, and science instrument frames.


Version and Date
========================================================================

   Version 0.1 -- March 3, 2011 -- Boris Semenov, NAIF

      Preliminary version. Added rover and cruise/descent frames.

   Version 0.0 -- December 4, 2007 -- Boris Semenov, NAIF

      Very preliminary version. Contains definitions only for the 
      LOCAL_LEVEL and SURFACE_FIXED frames.
 

References
========================================================================

   1. ``Frames Required Reading''

   2. ``Kernel Pool Required Reading''

   3. ``C-Kernel Required Reading''


Contact Information
========================================================================

   Boris V. Semenov, NAIF/JPL, (818)-354-8136, Boris.Semenov@jpl.nasa.gov


Implementation Notes
========================================================================

   This file is used by the SPICE system as follows: programs that make
   use of this frame kernel must `load' the kernel using SPICE routine
   FURNSH, normally during program initialization.

   This file was created and may be updated with a text editor or word
   processor.


MSL NAIF ID Codes
========================================================================

   The following names and NAIF ID codes are assigned to the MSL rover,
   its structures and science instruments (the keywords implementing
   these definitions are located in the section "MSL Mission NAIF ID
   Codes -- Definition Section" at the end of this file):
 
   MSL rover, landing site, and sites:
   -------------------------------------

      MSL                      -76
      MSL_LANDING_SITE         -76900

      MSL_ROVER                -76000

      MSL_SPACECRAFT           -76010
      MSL_CRUISE_STAGE         -76020
      MSL_DESCENT_STAGE        -76030
      MSL_ROVER_MECH           -76040


MSL Frames
========================================================================

   The following MSL frames are defined in this kernel file:

           Name                      Relative to         Type    NAIF ID
      ======================     ===================     =====   =======

   MSL Surface frames:
   ---------------------

      MSL_TOPO                   IAU_MARS                FIXED   -76900
      MSL_LOCAL_LEVEL            MSL_TOPO                FIXED   -76910
      MSL_SURFACE_FIXED          MSL_LOCAL_LEVEL         FIXED   -76920

   MSL Rover frames:
   -----------------

      MSL_ROVER                  J2000, LOCAL_LEVEL      CK      -76000
      MSL_ROVER_MECH             MSL_ROVER               FIXED   -76040

   MSL Cruise and Descent frames:
   ------------------------------

      MSL_SPACECRAFT             MSL_ROVER               FIXED   -76010
      MSL_CRUISE_STAGE           MSL_ROVER               FIXED   -76020
      MSL_DESCENT_STAGE          MSL_ROVER               FIXED   -76030


MSL Frame Tree
========================================================================

   The diagram below shows the MSL frame hierarchy:


                                   "J2000" 
                      +-------------------------------+
                      |               |<-pck          |<-pck
                      |               v               v
                      |          "IAU_MARS"       "IAU_EARTH"
                      |          ----------       -----------
                      |               |<-fixed      
                      |               v             
                      |          "MSL_TOPO"      "MSL_SURFACE_FIXED"
                      |          ----------      ---------------------
                      |               |<-fixed        ^<-fixed
                      |               v               |
                      |       "MSL_LOCAL_LEVEL"       |
                      |       ------------------------+
                      |               |                      
                      |               |     "MSL_SPACECRAFT"
                      |               |     ----------------
                      |               |      ^ <-fixed
                      |               |      |
                      |               |      | "MSL_CRUISE_STAGE"
                      |               |      | ------------------
                      |               |      |  ^ <-fixed
                      |               |      |  |
                      |               |      |  | "MSL_DESCENT_STAGE"
                      |               |      |  | ------------------
                      |               |      |  |  ^ <-fixed
                      |               |      |  |  |
                      |               |      |  |  | "MSL_ROVER_MECH"
                      |               |      |  |  | ----------------
                      |               |      |  |  |  ^ <-fixed
                 ck-> |          ck-> |      |  |  |  |             
                      v               v                      
                                 "MSL_ROVER"
                     -----------------------------------


MSL Surface Frames
========================================================================

   The surface frames layout in this version of the FK is based on the
   assumption that the total traverse distance during the mission will
   be relatively short (hundreds of meters, not kilometers) and,
   therefore, the local north and nadir directions, defining surface
   frame orientations, will be approximately the same at any point
   along the traverse path. This assumption allows defining surface
   frames as fixed offset frames with respect to each other and/or to
   Mars body-fixed frame, IAU_MARS.


MSL Topocentric Frame
-----------------------

   MSL topocentric frame, MSL_TOPO, is defined as follows:

      -- +Z axis is along the outward normal at the landing site ("zenith");

      -- +X axis is along the local north direction ("north");

      -- +Y axis completes the right hand frame ("west");

      -- the origin of this frame is located at the landing site.

   Orientation of the frame is given relative to the body fixed
   rotating frame 'IAU_MARS' (x - along the line of zero longitude
   intersecting the equator, z - along the spin axis, y - completing
   the right hand coordinate frame.)

   The transformation from 'MSL_TOPO' frame to 'IAU_MARS' frame is a
   3-2-3 rotation with defined angles as the negative of the site
   longitude, the negative of the site colatitude, 180 degrees.

   This frame is defined in a separate FK that must be loaded together
   with this FK.


MSL Local Level Frame
-----------------------

   MSL local level frame, MSL_LOCAL_LEVEL, is defined as follows:

      -- +Z axis is along the downward normal at the landing site ("nadir");

      -- +X axis is along the local north direction ("north");

      -- +Y axis completes the right hand frame ("east");

      -- the origin of this frame is located between the rover's middle
         wheels and moves with the rover.
 
   Since this frame is essentially the MSL_TOPO frame flipped by 180
   degrees about +X ("north") to point +Z down, this frame is defined
   as a fixed offset frame with respect to the MSL_TOPO frame.

   \begindata

      FRAME_MSL_LOCAL_LEVEL           = -76910
      FRAME_-76910_NAME               = 'MSL_LOCAL_LEVEL'
      FRAME_-76910_CLASS              = 4
      FRAME_-76910_CLASS_ID           = -76910
      FRAME_-76910_CENTER             = -76900
      TKFRAME_-76910_RELATIVE         = 'MSL_TOPO'
      TKFRAME_-76910_SPEC             = 'ANGLES'
      TKFRAME_-76910_UNITS            = 'DEGREES'
      TKFRAME_-76910_AXES             = (   1,       2,       3     )
      TKFRAME_-76910_ANGLES           = ( 180.000,   0.000,   0.000 )

   \begintext


MSL Surface Fixed Frame
-------------------------

   MSL surface fixed frame -- MSL_SURFACE_FIXED -- is nominally
   co-aligned in orientation with the MSL_LOCAL_LEVEL frame but its
   origin does not move during the mission. Therefore, this frame is
   defined as a zero-offset, fixed frame with respect to the MSL_LOCAL_LEVEL
   frame.

   \begindata

      FRAME_MSL_SURFACE_FIXED         = -76920
      FRAME_-76920_NAME               = 'MSL_SURFACE_FIXED'
      FRAME_-76920_CLASS              = 4
      FRAME_-76920_CLASS_ID           = -76920
      FRAME_-76920_CENTER             = -76900
      TKFRAME_-76920_RELATIVE         = 'MSL_LOCAL_LEVEL'
      TKFRAME_-76920_SPEC             = 'ANGLES'
      TKFRAME_-76920_UNITS            = 'DEGREES'
      TKFRAME_-76920_AXES             = (   1,       2,       3     )
      TKFRAME_-76920_ANGLES           = (   0.000,   0.000,   0.000 )

   \begintext


MSL Rover Frames
========================================================================

   The MSL rover NAV frame is defined as follows:

      -  +X points to the front of the rover, away from RTG
  
      -  +Z points down

      -  +Y completes the right handed frame

      -  the origin on this frame is between the rover middle wheels
         (midpoint between and on the rotation axis of the middle
         wheels for deployed rover and suspension system on flat plane.

   The orientation of this frame relative to other frames (J2000,
   MSL_LOCAL_LEVEL) changes in time and is provided in CK files.
   Therefore this frame is defined as a CK-based frame.

   \begindata

      FRAME_MSL_ROVER                 = -76000
      FRAME_-76000_NAME               = 'MSL_ROVER'
      FRAME_-76000_CLASS              = 3
      FRAME_-76000_CLASS_ID           = -76000
      FRAME_-76000_CENTER             = -76
      CK_-76000_SCLK                  = -76
      CK_-76000_SPK                   = -76

   \begintext

   The MSL rover mechanical frame -- MSL_ROVER_MECH -- is nominally
   co-aligned in orientation with the rover NAV frame, MSL_ROVER, and is
   defined below as fixed, zero-offset frame relative to it. The origin
   of this frame is different from the rover NAV frame origin and is
   translated from it by a fixed offset along the Z axis, provided in
   the MSL structures SPK file.

   \begindata

      FRAME_MSL_ROVER_MECH            = -76040
      FRAME_-76040_NAME               = 'MSL_ROVER_MECH'
      FRAME_-76040_CLASS              = 4
      FRAME_-76040_CLASS_ID           = -76040
      FRAME_-76040_CENTER             = -76
      TKFRAME_-76040_RELATIVE         = 'MSL_ROVER'
      TKFRAME_-76040_SPEC             = 'ANGLES'
      TKFRAME_-76040_UNITS            = 'DEGREES'
      TKFRAME_-76040_AXES             = (   1,       2,       3     )
      TKFRAME_-76040_ANGLES           = (   0.000,   0.000,   0.000 )

   \begintext


MSL Cruise and Descent Frames
========================================================================

   The MSL cruise and descent frames -- MSL_SPACECRAFT,
   MSL_CRUISE_STAGE, and MSL_DESCENT_STAGE -- are nominally co-aligned
   in orientation with the rover NAV frame, MSL_ROVER, and are defined
   below as fixed, zero-offset frames relative to it. The origins of
   these frames are different from the rover NAV frame origin and are
   translated from it fixed offsets along the Z axis, provided in the
   MSL structures SPK file.

   \begindata

      FRAME_MSL_SPACECRAFT            = -76010
      FRAME_-76010_NAME               = 'MSL_SPACECRAFT'
      FRAME_-76010_CLASS              = 4
      FRAME_-76010_CLASS_ID           = -76010
      FRAME_-76010_CENTER             = -76
      TKFRAME_-76010_RELATIVE         = 'MSL_ROVER'
      TKFRAME_-76010_SPEC             = 'ANGLES'
      TKFRAME_-76010_UNITS            = 'DEGREES'
      TKFRAME_-76010_AXES             = (   1,       2,       3     )
      TKFRAME_-76010_ANGLES           = (   0.000,   0.000,   0.000 )

      FRAME_MSL_CRUISE_STAGE          = -76020
      FRAME_-76020_NAME               = 'MSL_CRUISE_STAGE'
      FRAME_-76020_CLASS              = 4
      FRAME_-76020_CLASS_ID           = -76020
      FRAME_-76020_CENTER             = -76
      TKFRAME_-76020_RELATIVE         = 'MSL_ROVER'
      TKFRAME_-76020_SPEC             = 'ANGLES'
      TKFRAME_-76020_UNITS            = 'DEGREES'
      TKFRAME_-76020_AXES             = (   1,       2,       3     )
      TKFRAME_-76020_ANGLES           = (   0.000,   0.000,   0.000 )

      FRAME_MSL_DESCENT_STAGE         = -76030
      FRAME_-76030_NAME               = 'MSL_DESCENT_STAGE'
      FRAME_-76030_CLASS              = 4
      FRAME_-76030_CLASS_ID           = -76030
      FRAME_-76030_CENTER             = -76
      TKFRAME_-76030_RELATIVE         = 'MSL_ROVER'
      TKFRAME_-76030_SPEC             = 'ANGLES'
      TKFRAME_-76030_UNITS            = 'DEGREES'
      TKFRAME_-76030_AXES             = (   1,       2,       3     )
      TKFRAME_-76030_ANGLES           = (   0.000,   0.000,   0.000 )

   \begintext


MSL NAIF ID Codes -- Definition Section
========================================================================

   This section contains name to NAIF ID mappings for the MSL.


MSL rover and landing site names and IDs
------------------------------------------

   \begindata

      NAIF_BODY_NAME += ( 'MSL'                      )
      NAIF_BODY_CODE += ( -76                        )

      NAIF_BODY_NAME += ( 'MSL_LANDING_SITE'         )
      NAIF_BODY_CODE += ( -76900                     )

      NAIF_BODY_NAME += ( 'MSL_ROVER'                )
      NAIF_BODY_CODE += ( -76000                     )

      NAIF_BODY_NAME += ( 'MSL_SPACECRAFT'           )
      NAIF_BODY_CODE += ( -76010                     )

      NAIF_BODY_NAME += ( 'MSL_CRUISE_STAGE'         )
      NAIF_BODY_CODE += ( -76020                     )

      NAIF_BODY_NAME += ( 'MSL_DESCENT_STAGE'        )
      NAIF_BODY_CODE += ( -76030                     )

      NAIF_BODY_NAME += ( 'MSL_ROVER_MECH'           )
      NAIF_BODY_CODE += ( -76040                     )


   \begintext

End of FK file.
