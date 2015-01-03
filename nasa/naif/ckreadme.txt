 
MSL CK files.
===========================================================================
 
     This ``aareadme.txt'' file describes contents of the kernels/ck
     directory of the MSL SPICE data server. It was last modified on June
     10, 2013. Contact Chuck Acton (818-354-3869, Chuck.Acton@jpl.nasa.gov)
     or Boris Semenov (818-354-8136, Boris.Semenov@jpl.nasa.gov) if you
     have any questions regarding this data.
 
 
If you are in a hurry ...
--------------------------------------------------------
 
     ... copy and use these CK files to get the complete set of the best
     and latest MSL CK data:
 
        msl_ra_toolsref_v1.bc
        msl_cruise_recon_rawrt_v2.bc
        msl_cruise_recon_raweng_v1.bc
        msl_edl_v01.bc
        msl_surf_hga_tlm.bc
        msl_surf_ra_tlmenc.bc
        msl_surf_ra_tlmres.bc
        msl_surf_rsm_tlmenc.bc
        msl_surf_rsm_tlmres.bc
        msl_surf_rover_tlm.bc
 
 
Brief summary
--------------------------------------------------------
 
     This directory contains the SPICE C-Kernel files for the MSL mission.
     The following files are present in this directory. (Only file names
     are shown; the file name extensions of the binary CK files are always
     ``.bc'')
 
 
Ever-growing MSL Surface Rover Orientation CK Files
--------------------------------------------------------
 
     These CKs contains the latest surface OPS data.
 
           msl_surf_rover_tlm                an ever-growing CK file
                                             containing the orientation of
                                             the rover relative to the
                                             local level frame, created
                                             using telemetry view data from
                                             OPGS PLACES server, covering
                                             from SOL 0000 through the
                                             current SOL, updated twice
                                             each SOL at 06:30 and 18:30
                                             LMST. This file has gaps in
                                             its coverage.
 
           msl_surf_rover_tlm_spanned        version of
                                             msl_surf_rover_tlm.bc modified
                                             using CKSPANIT to enforce
                                             interpolation over the whole
                                             time range covered by the
                                             file.
 
           msl_surf_rover_runout             CK file extrapolating the
                                             latest rover orientation data
                                             point in msl_surf_rover_tlm.bc
                                             for ~10 years into the future.
                                             This CK file can be used for
                                             predictive analysis
                                             applications ONLY if it is
                                             known that the rover did not
                                             (yet) change its orientation
                                             from the one provided by that
                                             data point. As soon as the
                                             rover changes its orientation
                                             the prediction provided by
                                             this CK file becomes invalid.
 
 
Ever-growing MSL Surface Articulating Structures Orientation CK Files
--------------------------------------------------------
 
     These CKs contains the latest surface OPS data.
 
           msl_surf_hga_tlm                  an ever-growing CK file
                                             containing the orientation of
                                             the High Gain Antenna (HGA)
                                             relative to the rover frame,
                                             created using telemetry view
                                             data from OPGS PLACES server,
                                             covering from SOL 0000 through
                                             the current SOL, updated twice
                                             each SOL at 06:30 and 18:30
                                             LMST. This file has gaps in
                                             its coverage.
 
           msl_surf_hga_tlm_spanned          version of msl_surf_hga_tlm.bc
                                             modified using CKSPANIT to
                                             enforce interpolation over the
                                             whole time range covered by
                                             the file.
 
           msl_surf_ra_tlmenc                an ever-growing CK file
                                             containing the orientation of
                                             the Robotic Arm (RA) relative
                                             to the rover frame, created
                                             using encoder angles from
                                             telemetry view data from OPGS
                                             PLACES server, covering from
                                             SOL 0000 through the current
                                             SOL, updated twice each SOL at
                                             06:30 and 18:30 LMST. This
                                             file has gaps in its coverage.
 
           msl_surf_ra_tlmenc_spanned        version of
                                             msl_surf_ra_tlmenc.bc modified
                                             using CKSPANIT to enforce
                                             interpolation over the whole
                                             time range covered by the
                                             file.
 
           msl_surf_ra_tlmres                an ever-growing CK file
                                             containing the orientation of
                                             the Robotic Arm (RA) relative
                                             to the rover frame, created
                                             using resolver angles from
                                             telemetry view data from OPGS
                                             PLACES server, covering from
                                             SOL 0000 through the current
                                             SOL, updated twice each SOL at
                                             06:30 and 18:30 LMST. This
                                             file has gaps in its coverage.
                                             This CK is more accurate
                                             compared to the encoder based
                                             RA CK but generally provides
                                             less coverage.
 
           msl_surf_ra_tlmres_spanned        version of
                                             msl_surf_ra_tlmres.bc modified
                                             using CKSPANIT to enforce
                                             interpolation over the whole
                                             time range covered by the
                                             file.
 
           msl_surf_rsm_tlmenc               an ever-growing CK file
                                             containing the orientation of
                                             the Remote Sensing Mast (RSM)
                                             relative to the rover frame,
                                             created using encoder angles
                                             telemetry view data from OPGS
                                             PLACES server, covering from
                                             SOL 0000 through the current
                                             SOL, updated twice each SOL at
                                             06:30 and 18:30 LMST. This
                                             file has gaps in its coverage.
 
           msl_surf_rsm_tlmenc_spanned       version of
                                             msl_surf_rsm_tlmenc.bc
                                             modified using CKSPANIT to
                                             enforce interpolation over the
                                             whole time range covered by
                                             the file.
 
           msl_surf_rsm_tlmres               an ever-growing CK file
                                             containing the orientation of
                                             the Remote Sensing Mast (RSM)
                                             relative to the rover frame,
                                             created using resolver angles
                                             from telemetry view data from
                                             OPGS PLACES server, covering
                                             from SOL 0000 through the
                                             current SOL, updated twice
                                             each SOL at 06:30 and 18:30
                                             LMST. This file has gaps in
                                             its coverage. This CK is more
                                             accurate compared to the
                                             encoder based RSM CK but
                                             generally provides less
                                             coverage.
 
           msl_surf_rsm_tlmres_spanned       version of
                                             msl_surf_rsm_tlmres.bc
                                             modified using CKSPANIT to
                                             enforce interpolation over the
                                             whole time range covered by
                                             the file.
 
 
Archival MSL Cruise Orientation CK Files
--------------------------------------------------------
 
     These CKs contain cruise orientation solutions from various sources.
 
           msl_cruise_recon_nospin           CK file containing complete
                                             cruise orientation based on
                                             H-vector direction without
                                             modeling spin, provided by the
                                             MSL ACS Team.
 
           msl_cruise_recon_rawrt_vV         CK files containing fully
                                             reconstructed orientation
                                             based on real-time quaternions
                                             covering only communication
                                             periods, provided by the MSL
                                             ACS Team.
 
           msl_cruise_recon_raweng_vV        CK files containing fully
                                             reconstructed orientation
                                             based on high-rate quaternions
                                             covering only short periods
                                             around thruster firing events
                                             and ACS calibration
                                             activities, provided by the
                                             MSL ACS Team.
 
           msl_cruise_recon_nav_vV           CK file containing fully
                                             reconstructed orientation
                                             based on H-vector direction
                                             and NAV de-spinned tracking
                                             data, covering only
                                             communication periods,
                                             provided by the MSL NAV Team.
 
 
Archival MSL EDL Orientation CK Files
--------------------------------------------------------
 
     These CKs contain EDL orientation solutions from various sources.
 
           msl_edl_vV                        CK files containing the
                                             reconstructed EDL orientation
                                             determined by the MSL EDL
                                             Team, JPL. The file with the
                                             latest version number V
                                             supersedes all previous
                                             versions.
 
 
Archival MSL Surface Rover Orientation CK Files
--------------------------------------------------------
 
     These CKs contain the same data as the corresponding ever-growing OPS
     CK, sub-setted for the archive release coverage ranges.
 
           msl_surf_rover_tlm_BBBB_EEEE_vV   CK files containing the
                                             orientation of the rover
                                             relative to the local level
                                             frame, created using telemetry
                                             view data from OPGS PLACES
                                             server, covering SOLs "BBBB"
                                             to "EEEE", version V.
 
 
Archival MSL Surface Articulating Structures Orientation CK Files
--------------------------------------------------------
 
     These CKs contain the same data as the corresponding ever-growing OPS
     CKs, sub-setted for the archive release coverage ranges.
 
           msl_surf_hga_tlm_BBBB_EEEE_vV     CK files containing the
                                             orientation of the High Gain
                                             Antenna (HGA) relative to the
                                             rover frame, created using
                                             telemetry view data from OPGS
                                             PLACES server, covering SOLs
                                             "BBBB" to "EEEE", version V.
 
           msl_surf_ra_tlmenc_BBBB_EEEE_vV   CK files containing the
                                             orientation of the Robotic Arm
                                             (RA) relative to the rover
                                             frame, created using encoder
                                             angles from telemetry view
                                             data from OPGS PLACES server,
                                             covering SOLs "BBBB" to
                                             "EEEE", version V.
 
           msl_surf_ra_tlmres_BBBB_EEEE_vV   CK files containing the
                                             orientation of the Robotic Arm
                                             (RA) relative to the rover
                                             frame, created using resolver
                                             angles from telemetry view
                                             data from OPGS PLACES server,
                                             covering SOLs "BBBB" to
                                             "EEEE", version V. These CKs
                                             are more accurate compared to
                                             the encoder based RA CK but
                                             generally provide less
                                             coverage.
 
           msl_surf_rsm_tlmenc_BBBB_EEEE_v   CK files containing the
                                             orientation of the Remote
                                             Sensing Mast (RSM) relative to
                                             the rover frame, created using
                                             encoder angles telemetry view
                                             data from OPGS PLACES server,
                                             covering SOLs "BBBB" to
                                             "EEEE", version V.
 
           msl_surf_rsm_tlmres_BBBB_EEEE_v   CK files containing the
                                             orientation of the Remote
                                             Sensing Mast (RSM) relative to
                                             the rover frame, created using
                                             resolver angles from telemetry
                                             view data from OPGS PLACES
                                             server, covering SOLs "BBBB"
                                             to "EEEE", version V. These
                                             CKs are more accurate compared
                                             to the encoder based RSM CKs
                                             but generally provide less
                                             coverage.
 
 
MSL RA Tool REF-to-Instrument Offsets CK Files
--------------------------------------------------------
 
     These CKs contain actual alignment data for some of the instruments.
     They are intended to provide the connections to these instruments'
     frames.
 
           msl_ra_toolsref_vV                CK files containing fixed
                                             orientations of the RA
                                             instrument frames relative to
                                             their corresponding REF
                                             frames. This is an auxiliary
                                             CK needed to complete the
                                             frame chain from the rover to
                                             the instruments mounted on RA.
 
 
Special Pose CK Files
--------------------------------------------------------
 
     These CKs contain actual pose data for certain appendage positions.
     They are intended solely to provide the connections between various
     frames needed in some test scenarios in which these positions were
     utilized.
 
           msl_ra_straight_vV                CK files containing the fixed
                                             orientation of the RA joints
                                             for the RA in the ``straight
                                             out'' position.
 
           msl_rsm_calpose_vV                CK files containing the fixed
                                             orientation of the RSM joints
                                             for the RSM in the
                                             ``calibration pose'' position.
 
 
Special Purpose ``Connection'' CK Files
--------------------------------------------------------
 
     These CKs contain nominal orientation data. They are intended solely
     to provide the connections between various frames needed in some test
     scenarios.
 
           msl_hga_zero_vV                   CK files containing the fixed
                                             orientation of the HGA gimbals
                                             for the HGA in the ``zero''
                                             gimbal position.
 
           msl_rover2cacs_vV                 CK files containing the fixed
                                             orientation of the rover frame
                                             relative to the CACS frame.
 
           msl_still_at_ls_v1                CK files containing the fixed,
                                             co-aligned orientation of the
                                             rover relative to the local
                                             level frame for.
 
