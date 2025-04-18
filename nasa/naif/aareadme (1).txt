 
MSL SCLK files.
===========================================================================
 
     This ``aareadme.txt'' file describes contents of the kernels/sclk
     directory of the MSL SPICE data server. It was last modified on June
     10, 2013. Contact Chuck Acton (818-354-3869, Chuck.Acton@jpl.nasa.gov)
     or Boris Semenov (818-354-8136, Boris.Semenov@jpl.nasa.gov) if you
     have any questions regarding this data.
 
 
If you are in a hurry ...
--------------------------------------------------------
 
     ... copy and use these SCLK files to get the complete set of the best
     and latest MSL SCLK data:
 
        msl_lmst_ops120808_v1.tsc
        msl.tsc
 
 
Brief summary
--------------------------------------------------------
 
     This directory contains the SPICE SCLK files for the MSL rover
     on-board clock and SCLK files implementing MSL Local Mean Solar Time
     (LMST). All files are in IEEE text format and must be copied using ftp
     ASCII transfer to any non-UNIX workstation. The following files are
     present in this directory. (Only file names are shown; the file name
     extensions of the text SCLK files are always ".tsc"):
 
           MSL_76_SCLKSCET*VVVVV   MSL on-board clock coefficients Kernel
                                   (SCLK) files generated by converting
                                   corresponding SCLKvSCET file produced by
                                   TMOD Time Services to SPICE SCLK format
                                   using MAKCLK SPICE Utility program. In
                                   flight these files are created/delivered
                                   every time new SCLKvSCET file is
                                   delivered.
 
           msl                     default-name copy of the latest
                                   MSL_76_SCLKSCET.VVVVV.tsc file. The
                                   companion file msl.tsc.history shows the
                                   msl.tsc update history.
 
           msl_lmst_[id]_vV        MSL LMST SCLKs implementing MSL LMST for
                                   target and actual landing sites
                                   identified by [id]. The ``gc120806'' are
                                   SCLKs for the pre-landing Gusev Crater
                                   target landing sites. The ``ops120808''
                                   is the SCLK for the actual landing site.
                                   The comments inside these SCLK kernels
                                   show how to use them to convert between
                                   LMST and ET.
 
     If multiple versions of a SCLK file are provided, always use the
     latest version (unless earlier version is needed for some special
     reasons.)
 
