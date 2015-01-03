KPL/FK

MSL Target Site GC (Gale Crater, final target, revA) Topocentric Frame Definition Kernel
===============================================================================

     Created by Boris Semenov, NAIF/JPL, Thu Jun 14 14:56:24 PDT 2012


Frames in this File
--------------------------------------------------------

     This frames kernel defines the topocentric frame at the MSL
     landing site GC (Gale Crater, final target, revA):

        frame name .......... MSL_TOPO
        frame ID ............ -76900
        frame type........... FIXED
        relative to frame ... IAU_MARS

     This diagram shows the frame hierarchy:

                "J2000" INERTIAL
              --------------------
                       |
                       | <--pck
                       V
                   "IAU_MARS"
               BODY-FIXED ROTATING
              --------------------
                       |
                       |  <--fixed
                       V
                  "MSL_TOPO"
              --------------------


MSL Topocentric Frame Definition
-------------------------------------------------------------------------------

     This frame defines the z axis as the normal outward at the landing
     site, the x axis points at local north with the y axis completing
     the right handed frame (points at local west.)

     Orientation of the frame is given relative to the body fixed rotating
     frame 'IAU_MARS' (x - along the line of zero longitude intersecting
     the equator, z - along the spin axis, y - completing the right hand
     coordinate frame.)

     The transformation from 'MSL_TOPO' frame to 'IAU_MARS'
     frame is a 3-2-3 rotation with defined angles as the negative of
     the site longitude, the negative of the site colatitude, 180 degrees.

     The landing site Gaussian longitude and latitude upon which the
     definition is built are:

        Lon = 137.401900 degrees East
        Lat =  -4.650933 degrees North

     The coordinates specified above are given with respect to the
     'IAU_MARS' instance defined by the rotation/shape model from the
     the PCK file 'pck00008.tpc'.

     These keywords implement the frame definition.

        \begindata

         FRAME_MSL_TOPO             = -76900
	 FRAME_-76900_NAME           = 'MSL_TOPO'
	 FRAME_-76900_CLASS          =  4
	 FRAME_-76900_CLASS_ID       =  -76900
	 FRAME_-76900_CENTER         =  -76900

	 TKFRAME_-76900_RELATIVE     = 'IAU_MARS'
	 TKFRAME_-76900_SPEC         = 'ANGLES'
	 TKFRAME_-76900_UNITS        = 'DEGREES'
	 TKFRAME_-76900_AXES         = ( 3, 2, 3 )
	 TKFRAME_-76900_ANGLES       = ( -137.4019, -94.650933, 180.000  )

        \begintext


Landing Site Name to NAIF ID Mapping
--------------------------------------------------------

     This section contains keywords that map the lander and landing site
     names their NAIF IDs:

        \begindata

            NAIF_BODY_NAME += ( 'MSL' )
            NAIF_BODY_CODE += ( -76 )

            NAIF_BODY_NAME += ( 'MSL_LANDING_SITE' )
            NAIF_BODY_CODE += ( -76900 )

        \begintext


Contacts
--------------------------------------------------------

     If you have any questions regarding this data contact

        Boris V. Semenov, NAIF/JPL,
        (818)-354-8136,
        Boris.Semenov@jpl.nasa.gov

