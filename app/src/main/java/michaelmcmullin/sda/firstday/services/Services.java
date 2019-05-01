package michaelmcmullin.sda.firstday.services;

import michaelmcmullin.sda.firstday.interfaces.services.QrService;

/**
 * Class to store default services in one location to simplify updates.
 */
public class Services {

  /**
   * Service used for reading and writing QR codes.
   */
  public static QrService QrService = new FirebaseQr();
}
