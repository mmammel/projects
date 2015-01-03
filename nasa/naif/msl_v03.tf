KPL/FK

MSL Frames Kernel
========================================================================

   This frame kernel contains complete set of frame definitions for the
   MSL including definitions for the MSL cruise, descent, and rover
   frames, local level, topocentric and surface-fixed frames, appendage 
   frames, and science instrument frames.


Version and Date
========================================================================

   Version 0.3 -- May 4, 2011 -- Boris Semenov, NAIF

      Preliminary version. Changed MSL_TLGA tilt angle to 17.5 
      degrees.

   Version 0.2 -- May 3, 2011 -- Boris Semenov, NAIF

      Preliminary version. Added CACS and antenna frames.

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

   4. MSL 3PCS document, latest version


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
      MSL_CACS                 -76050

      MSL_PLGA                 -76810
      MSL_TLGA                 -76811
      MSL_PUHF                 -76812
      MSL_MGA                  -76813

      MSL_DLGA                 -76820
      MSL_DUHF                 -76821

      MSL_RLGA                 -76830
      MSL_RUHF                 -76840
      MSL_HGA_BASE             -76850
      MSL_HGA_GIMBAL           -76851
      MSL_HGA                  -76852
      MSL_HGA_EB               -76853


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

      MSL_ROVER                  J2000, MSL_LOCAL_LEVEL  CK      -76000
      MSL_ROVER_MECH             MSL_ROVER               FIXED   -76040

   MSL Cruise and Descent frames:
   ------------------------------

      MSL_SPACECRAFT             MSL_ROVER               FIXED   -76010
      MSL_CRUISE_STAGE           MSL_ROVER               FIXED   -76020
      MSL_DESCENT_STAGE          MSL_ROVER               FIXED   -76030
      MSL_CACS                   J2000, MSL_ROVER        CK      -76050

   MSL Antenna frames:
   -------------------

      MSL_PLGA                   MSL_CRUISE_STAGE        FIXED   -76810
      MSL_TLGA                   MSL_CRUISE_STAGE        FIXED   -76811
      MSL_PUHF                   MSL_CRUISE_STAGE        FIXED   -76812
      MSL_MGA                    MSL_CRUISE_STAGE        FIXED   -76813

      MSL_DLGA                   MSL_DESCENT_STAGE       FIXED   -76820
      MSL_DUHF                   MSL_DESCENT_STAGE       FIXED   -76821

      MSL_RLGA                   MSL_ROVER               FIXED   -76830
      MSL_RUHF                   MSL_ROVER               FIXED   -76840

      MSL_HGA_BASE               MSL_ROVER               FIXED   -76850
      MSL_HGA_GIMBAL             MSL_HGA_BASE            FIXED   -76851
      MSL_HGA                    MSL_HGA_GIMBAL          CK      -76852
      MSL_HGA_EB                 MSL_HGA                 FIXED   -76853


MSL Frame Tree
========================================================================

   The diagram below shows the MSL frame hierarchy:


                                   "J2000" 
                      +---------------------------------+
                      |               |<-pck            |<-pck
                      |               v                 v
                      |          "IAU_MARS"       "IAU_EARTH"
                      |          ----------       -----------
                      |               |<-fixed      
                      |               v             
                      |          "MSL_TOPO"      "MSL_SURFACE_FIXED"
                      |          ----------      ---------------------
                      |               |<-fixed          ^<-fixed
                      |               v                 |
                      |       "MSL_LOCAL_LEVEL"         |
                      |       --------------------------+
                      |               |                      
                      |               |                   "MSL_DLGA/DUHF"
                      |               |                   ---------------
                      |               |                     fixed-> ^
                      |               |                             |
                      |               |  "MSL_PLGA/TLGA/PUHF/MGA"   |
                      |               |  ------------------------   |
                      |               |               fixed-> ^     |
                      |               |                       |     |
                      |               |  "MSL_SPACECRAFT"     |     |
                      |               |  ----------------     |     |
                      |               |     ^ <-fixed         |     |
                      |               |     |                 |     | 
                      |               |     |   "MSL_CRUISE_STAGE"  |
                      |               |     |   ------------------  |
                      |               |     |     ^ <-fixed         |
                      |               |     |     |                 |
                      |               |     |     |   "MSL_DESCENT_STAGE"
                      |               |     |     |   ------------------
                 ck-> |               |     |     |     ^ <-fixed
                      |               |     |     |     |
                 "MSL_CACS"           |     |     |     |  "MSL_ROVER_MECH"
                 ----------           |     |     |     |  ----------------
                      ^               |     |     |     |     ^ <-fixed
                 ck-> |          ck-> |     |     |     |     |             
                      v               v                      
                                 "MSL_ROVER"
     +--------------------------------------------------------------+
     |                                                        |     | 
     |<-fixed                                         fixed-> |     | 
     v                                                        v     |
   "MLS_RUHF"                                            "MLS_RLGA" |
   ----------                                            ---------- |
                                                                    | 
                                                                    | <-fixed
                                                                    V 
                                                               "MSL_HGA_BASE"
                                                               --------------
                                                                    | 
                                                                    | <-fixed
                                                                    V 
                                                              "MSL_HGA_GIMBAL"
                                                              ----------------
                                                                    | 
                                                                    | <-ck
                                                                    V 
                                                                "MSL_HGA"
                                                                ---------
                                                                    | 
                                                                    | <-fixed
                                                                    V 
                                                              "MSL_HGA_EB"
                                                              ------------



   
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

   The MSL rover NAV frame, MSL_ROVER, is defined as follows:

      -  +X points to the front of the rover, away from RTG
  
      -  +Z points down

      -  +Y completes the right handed frame

      -  the origin on this frame is between the rover middle wheels
         (midpoint between and on the rotation axis of the middle
         wheels for deployed rover and suspension system on flat plane.

   The MSL rover NAV frame is shown on this diagram:

      TBD

   The orientation of this frame relative to other frames (J2000,
   MSL_LOCAL_LEVEL) changes in time and is provided in CK files.
   Therefore the MSL_ROVER frame is defined as a CK-based frame.

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
   co-aligned in orientation with the rover NAV frame, MSL_ROVER. The
   origin of this frame is different from the rover NAV frame origin
   and is translated from it by a fixed offset along the Z axis,
   provided in the MSL structures SPK file.

   The MSL rover mechanical frame is shown on this diagram:

      TBD

   The MSL_ROVER_MECH frame is defined below as fixed, zero-offset
   frame relative to the MSL_ROVER frame.
 
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

   The following three MSL cruise and descent frames -- MSL_SPACECRAFT,
   MSL_CRUISE_STAGE, and MSL_DESCENT_STAGE -- are nominally co-aligned
   in orientation with the rover NAV frame, MSL_ROVER. The origins of
   these frames are different from the rover NAV frame origin and are
   translated from it fixed offsets along the Z axis, provided in the
   MSL structures SPK file.

   These three frames are shown on this diagram:

      TBD

   These frames are are defined below as fixed, zero-offset frames
   relative to the MSL_ROVER frame.

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

   The MSL Cruise ACS frame, MSL_CACS, is defined as follows:

      -  +Z is co-aligned with the +Z axis of the MSL_ROVER frame
  
      -  +Y is directly over the location of the cruise stage star
         scanner

      -  +X completes the right handed frame and is directly over the
         B-string thruster cluster

      -  the origin on this frame is the same as of the MSL_SPACECRAFT
         frame.

   Nominally this frame is rotated +135 degrees about +Z from the
   MSL_ROVER frame. 

   The MSL CACS frame is shown on this diagram:

      TBD

   Because during cruise the orientation of this frame
   relative the J2000 frame comes in telemetry and is provided in CK
   files, this frame is defined as a CK-based frame.

   \begindata

      FRAME_MSL_CACS                  = -76050
      FRAME_-76050_NAME               = 'MSL_CACS'
      FRAME_-76050_CLASS              = 3
      FRAME_-76050_CLASS_ID           = -76050
      FRAME_-76050_CENTER             = -76
      CK_-76050_SCLK                  = -76
      CK_-76050_SPK                   = -76

   \begintext


MSL Antenna Frames
========================================================================

   This section defines frames for the MSL antennas -- Cruise X-band
   Parachute Cone Low Gain Antenna (PLGA), Cruise X-band Tilted Low
   Gain Antenna (TLGA), Cruise Parachute Cone UHF Antenna (PUHF),
   Cruise Medium Gain Antenna (MGA), X-band Descent Stage Low Gain
   Antenna (DLGA), Descent Stage UHF Antenna (DUHF), X-band Rover Low
   Gain Antenna (RLGA), Rover UHF Antenna (RUHF), X-band High Gain
   Antenna (HGA).

Cruise Antennas
---------------

   The frames for antennas mounted on the MSL cruise stage and
   parachute capsule -- MSL_PLGA, MSL_TLGA, MSL_PUHF, MSL_MGA -- are
   fixed with respect to the cruise stage frame, MSL_CRUISE_STAGE, and
   defined as follows:

      -  +Z is the antenna boresight, which is nominally along the
         cruise stage -Z except for TLGA
  
      -  +X is nominally co-aligned with the the cruise stage +X,
         except for TLGA

      -  +Y completes the right handed frame

      -  the origin is at the center of the antenna side farthest from
         the mounting plate (rim, tip, etc).

   The MSL_PLGA, MSL_PUHF, and MSL_MGA frames are rotated by 180 degrees 
   about +X from the MSL_CRUISE_STAGE frame.

   The MSL_TLGA is first rotated by 180 degrees about +X, then by -17.5
   (TBD) degrees about +Y from the MSL_CRUISE_STAGE frame.

   These four frames are shown on this diagram:

      TBD

   These frames are are defined below as fixed frames relative to the
   MSL_CRUISE_STAGE frame.

   \begindata

      FRAME_MSL_PLGA                  = -76810
      FRAME_-76810_NAME               = 'MSL_PLGA'
      FRAME_-76810_CLASS              = 4
      FRAME_-76810_CLASS_ID           = -76810
      FRAME_-76810_CENTER             = -76
      TKFRAME_-76810_RELATIVE         = 'MSL_CRUISE_STAGE'
      TKFRAME_-76810_SPEC             = 'ANGLES'
      TKFRAME_-76810_UNITS            = 'DEGREES'
      TKFRAME_-76810_AXES             = (   1,       2,       3     )
      TKFRAME_-76810_ANGLES           = ( 180.000,   0.000,   0.000 )

      FRAME_MSL_TLGA                  = -76811
      FRAME_-76811_NAME               = 'MSL_TLGA'
      FRAME_-76811_CLASS              = 4
      FRAME_-76811_CLASS_ID           = -76811
      FRAME_-76811_CENTER             = -76
      TKFRAME_-76811_RELATIVE         = 'MSL_CRUISE_STAGE'
      TKFRAME_-76811_SPEC             = 'ANGLES'
      TKFRAME_-76811_UNITS            = 'DEGREES'
      TKFRAME_-76811_AXES             = (   1,       2,       3     )
      TKFRAME_-76811_ANGLES           = ( 180.000,  17.500,   0.000 )

      FRAME_MSL_PUHF                  = -76812
      FRAME_-76812_NAME               = 'MSL_PUHF'
      FRAME_-76812_CLASS              = 4
      FRAME_-76812_CLASS_ID           = -76812
      FRAME_-76812_CENTER             = -76
      TKFRAME_-76812_RELATIVE         = 'MSL_CRUISE_STAGE'
      TKFRAME_-76812_SPEC             = 'ANGLES'
      TKFRAME_-76812_UNITS            = 'DEGREES'
      TKFRAME_-76812_AXES             = (   1,       2,       3     )
      TKFRAME_-76812_ANGLES           = ( 180.000,   0.000,   0.000 )

      FRAME_MSL_MGA                   = -76813
      FRAME_-76813_NAME               = 'MSL_MGA'
      FRAME_-76813_CLASS              = 4
      FRAME_-76813_CLASS_ID           = -76813
      FRAME_-76813_CENTER             = -76
      TKFRAME_-76813_RELATIVE         = 'MSL_CRUISE_STAGE'
      TKFRAME_-76813_SPEC             = 'ANGLES'
      TKFRAME_-76813_UNITS            = 'DEGREES'
      TKFRAME_-76813_AXES             = (   1,       2,       3     )
      TKFRAME_-76813_ANGLES           = ( 180.000,   0.000,   0.000 )

   \begintext


EDL Antennas
------------

   The frames for the antennas mounted on the MSL descent stage --
   MSL_DLGA, MSL_DUHF -- are fixed with respect to the descent stage
   frame, MSL_DESCENT_STAGE, and defined as follows:

      -  +Z is the antenna boresight, nominally along the descent stage
         -Z
  
      -  +X is nominally co-aligned with the the descent stage +X

      -  +Y completes the right handed frame

      -  the origin is at the center of the antenna side farthest from
         the mounting plate (rim, tip, etc).

   The MSL_DLGA and MSL_DUHF frames are rotated by 180 degrees about +X
   from the MSL_DESCENT_STAGE frame.

   These two frames are shown on this diagram:

      TBD

   These frames are are defined below as fixed frames relative to the
   MSL_DESCENT_STAGE frame.

   \begindata

      FRAME_MSL_DLGA                  = -76820
      FRAME_-76820_NAME               = 'MSL_DLGA'
      FRAME_-76820_CLASS              = 4
      FRAME_-76820_CLASS_ID           = -76820
      FRAME_-76820_CENTER             = -76
      TKFRAME_-76820_RELATIVE         = 'MSL_DESCENT_STAGE'
      TKFRAME_-76820_SPEC             = 'ANGLES'
      TKFRAME_-76820_UNITS            = 'DEGREES'
      TKFRAME_-76820_AXES             = (   1,       2,       3     )
      TKFRAME_-76820_ANGLES           = ( 180.000,   0.000,   0.000 )

      FRAME_MSL_DUHF                  = -76821
      FRAME_-76821_NAME               = 'MSL_DUHF'
      FRAME_-76821_CLASS              = 4
      FRAME_-76821_CLASS_ID           = -76821
      FRAME_-76821_CENTER             = -76
      TKFRAME_-76821_RELATIVE         = 'MSL_DESCENT_STAGE'
      TKFRAME_-76821_SPEC             = 'ANGLES'
      TKFRAME_-76821_UNITS            = 'DEGREES'
      TKFRAME_-76821_AXES             = (   1,       2,       3     )
      TKFRAME_-76821_ANGLES           = ( 180.000,   0.000,   0.000 )

   \begintext


Rover Antennas
--------------

   The frames for the two MSL non-articulating antennas mounted on the
   rover body -- MSL_RLGA, MSL_RUHF -- are fixed with respect to the
   rover frame, MSL_ROVER, and defined as follows:

      -  +Z is the antenna boresight, nominally along the rover -Z
  
      -  +X is nominally co-aligned with the the rover +X

      -  +Y completes the right handed frame

      -  the origin is at the center of the antenna side farthest from
         the mounting plate (rim, tip, etc).

   The MSL_RLGA and MSL_RUHF frames are rotated by 180 degrees about +X
   from the MSL_ROVER frame.

   These two frames are shown on this diagram:

      TBD

   These frames are are defined below as fixed frames relative to the
   MSL_ROVER frame.

   \begindata

      FRAME_MSL_RLGA                  = -76830
      FRAME_-76830_NAME               = 'MSL_RLGA'
      FRAME_-76830_CLASS              = 4
      FRAME_-76830_CLASS_ID           = -76830
      FRAME_-76830_CENTER             = -76
      TKFRAME_-76830_RELATIVE         = 'MSL_ROVER'
      TKFRAME_-76830_SPEC             = 'ANGLES'
      TKFRAME_-76830_UNITS            = 'DEGREES'
      TKFRAME_-76830_AXES             = (   1,       2,       3     )
      TKFRAME_-76830_ANGLES           = ( 180.000,   0.000,   0.000 )

      FRAME_MSL_RUHF                  = -76840
      FRAME_-76840_NAME               = 'MSL_RUHF'
      FRAME_-76840_CLASS              = 4
      FRAME_-76840_CLASS_ID           = -76840
      FRAME_-76840_CENTER             = -76
      TKFRAME_-76840_RELATIVE         = 'MSL_ROVER'
      TKFRAME_-76840_SPEC             = 'ANGLES'
      TKFRAME_-76840_UNITS            = 'DEGREES'
      TKFRAME_-76840_AXES             = (   1,       2,       3     )
      TKFRAME_-76840_ANGLES           = ( 180.000,   0.000,   0.000 )

   \begintext


   As defined in [4] the frame chain for the MSL articulating high gain
   antenna (HGA) includes four frames -- MSL_HGA_BASE, MSL_HGA_GIMBAL,
   MSL_HGA, and MSL_HGA_EB.

   The MSL_HGA_BASE frame (called HGAS frame in [4]) is a fixed offset
   frame nominally rotated from the MSL_ROVER frame by +25 degrees
   about +Z.

   The MSL_HGA_GIMBAL frame (called HGAG frame in [4]) is a fixed
   offset frame nominally rotated from the MSL_HGA_BASE frame by 180
   degrees about +X.

   The MSL_HGA frame (called ANT frame in [4]) is a CK-based frame
   rotated from the MSL_HGA_GIMBAL frame first by the azimuth angle
   about +Z, then by the elevation angle about +X. This frame's +Z axis
   is the antenna boresight defined as the normal to the antenna's
   radiating surface.

   The MSL_HGA_EB frame (called EB frame in [4]) is a fixed offset
   frame nominally rotated from the MSL_HGA frame by a 1.0 (TBD)
   degrees about +Y. This frame's +Z axis is the antenna boresight
   defined as the antenna electric boresight.

   The four HGA frames for the HGA in zero gimbal position are shown on
   this diagram:

      TBD

   These frames are are defined by the keyword sets below.

   \begindata

      FRAME_MSL_HGA_BASE              = -76850
      FRAME_-76850_NAME               = 'MSL_HGA_BASE'
      FRAME_-76850_CLASS              = 4
      FRAME_-76850_CLASS_ID           = -76850
      FRAME_-76850_CENTER             = -76
      TKFRAME_-76850_RELATIVE         = 'MSL_ROVER'
      TKFRAME_-76850_SPEC             = 'ANGLES'
      TKFRAME_-76850_UNITS            = 'DEGREES'
      TKFRAME_-76850_AXES             = (   1,       2,       3     )
      TKFRAME_-76850_ANGLES           = (   0.000,   0.000, -25.000 )

      FRAME_MSL_HGA_GIMBAL            = -76851
      FRAME_-76851_NAME               = 'MSL_HGA_GIMBAL'
      FRAME_-76851_CLASS              = 4
      FRAME_-76851_CLASS_ID           = -76851
      FRAME_-76851_CENTER             = -76
      TKFRAME_-76851_RELATIVE         = 'MSL_HGA_BASE'
      TKFRAME_-76851_SPEC             = 'ANGLES'
      TKFRAME_-76851_UNITS            = 'DEGREES'
      TKFRAME_-76851_AXES             = (   1,       2,       3     )
      TKFRAME_-76851_ANGLES           = ( 180.000,   0.000,   0.000 )

      FRAME_MSL_HGA                   = -76852
      FRAME_-76852_NAME               = 'MSL_HGA'
      FRAME_-76852_CLASS              = 3
      FRAME_-76852_CLASS_ID           = -76852
      FRAME_-76852_CENTER             = -76
      CK_-76852_SCLK                  = -76
      CK_-76852_SPK                   = -76

      FRAME_MSL_HGA_EB                = -76853
      FRAME_-76853_NAME               = 'MSL_HGA_EB'
      FRAME_-76853_CLASS              = 4
      FRAME_-76853_CLASS_ID           = -76853
      FRAME_-76853_CENTER             = -76
      TKFRAME_-76853_RELATIVE         = 'MSL_HGA'
      TKFRAME_-76853_SPEC             = 'ANGLES'
      TKFRAME_-76853_UNITS            = 'DEGREES'
      TKFRAME_-76853_AXES             = (   1,       2,       3     )
      TKFRAME_-76853_ANGLES           = (   0.000,  -1.000,   0.000 )

   \begintext


MSL NAIF ID Codes -- Definition Section
========================================================================

   This section contains name to NAIF ID mappings for the MSL.


MSL rover and landing site names and IDs
----------------------------------------

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


MSL Antenna names and IDs
-------------------------

   \begindata

      NAIF_BODY_NAME += ( 'MSL_PLGA'                 )
      NAIF_BODY_CODE += ( -76810                     )

      NAIF_BODY_NAME += ( 'MSL_TLGA'                 )
      NAIF_BODY_CODE += ( -76811                     )

      NAIF_BODY_NAME += ( 'MSL_PUHF'                 )
      NAIF_BODY_CODE += ( -76812                     )

      NAIF_BODY_NAME += ( 'MSL_MGA'                  )
      NAIF_BODY_CODE += ( -76813                     )

      NAIF_BODY_NAME += ( 'MSL_DLGA'                 )
      NAIF_BODY_CODE += ( -76820                     )
 
      NAIF_BODY_NAME += ( 'MSL_DUHF'                 )
      NAIF_BODY_CODE += ( -76821                     )

      NAIF_BODY_NAME += ( 'MSL_RLGA'                 )
      NAIF_BODY_CODE += ( -76830                     )

      NAIF_BODY_NAME += ( 'MSL_RUHF'                 )
      NAIF_BODY_CODE += ( -76840                     )

      NAIF_BODY_NAME += ( 'MSL_HGA_BASE'             )
      NAIF_BODY_CODE += ( -76850                     )

      NAIF_BODY_NAME += ( 'MSL_HGA_GIMBAL'           )
      NAIF_BODY_CODE += ( -76851                     )

      NAIF_BODY_NAME += ( 'MSL_HGA'                  )
      NAIF_BODY_CODE += ( -76852                     )

      NAIF_BODY_NAME += ( 'MSL_HGA_EB'               )
      NAIF_BODY_CODE += ( -76853                     )

   \begintext

End of FK file.
