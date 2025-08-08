package com.sleepythread.codepad;

class ParkingLot {
    public static void main(String[] args) {
        System.out.println("Parking Lot capacity: " + Parking.getTotalCapacity());
        System.out.println("Is parking lot empty ? -- " + (Parking.isParkingLotEmpty() ? "Yes" : "No"));
        Parking.Car.park();
        System.out.println("Is parking lot empty ? -- " + (Parking.isParkingLotEmpty() ? "Yes" : "No"));
        System.out.println("Parking Lot remaining capacity: " + Parking.getRemainingOpen());
        System.out.println("Parking lot van occupied spots: " + Parking.getVanOccupiedCount());
        Parking.Van.park();
        System.out.println("Parking lot van occupied spots: " + Parking.getVanOccupiedCount());
        System.out.println("Is parking lot full ? -- " + (Parking.isParkingLotFull() ? "Yes" : "No"));
        Parking.Van.park();
        Parking.Van.park();
        Parking.Van.park();
        System.out.println("Parking lot van occupied spots: " + Parking.getVanOccupiedCount());
    }

    enum Parking {
        Motorcycle(30),
        Van(3) {
            private int vanOccupancy;

            @Override
            public int getVanOccupancy() {
                return vanOccupancy;
            }

            @Override
            public boolean park() {
                if (this.remainingCapacity >= 1) {
                    remainingCapacity--;
                    vanOccupancy++;
                    return true;
                } else if (Car.getRemainingCapacity() >= 3) {
                    if (Car.park() && Car.park() && Car.park()) {
                        vanOccupancy += 3;
                        return true;
                    }
                }
                return false;
            }
        },
        Car(10);

        int remainingCapacity;
        int initialCapacity;

        private Parking(int capacity) {
            this.initialCapacity = capacity;
            this.remainingCapacity = capacity;
        }

        public boolean park() {
            if (this.remainingCapacity >= 1) {
                remainingCapacity--;
                return true;
            }
            return false;
        }

        public int leave() {
            if (remainingCapacity < initialCapacity) {
                remainingCapacity++;
            }
            return remainingCapacity;
        }

        public boolean isFull() {
            return remainingCapacity == 0;
        }

        public boolean isEmpty() {
            return remainingCapacity == initialCapacity;
        }

        public int getInitialCapacity() {
            return initialCapacity;
        }

        public int getRemainingCapacity() {
            return remainingCapacity;
        }

        public int getVanOccupancy() {
            return 0;
        }

        public static int getTotalCapacity() {
            return Van.getInitialCapacity() + Car.getInitialCapacity() + Motorcycle.getInitialCapacity();
        }

        public static boolean isParkingLotFull() {
            return Van.isFull() && Car.isFull() && Motorcycle.isFull();
        }

        public static boolean isParkingLotEmpty() {
            return Van.isEmpty() && Car.isEmpty() && Motorcycle.isEmpty();
        }

        public static int getRemainingOpen() {
            return Van.getRemainingCapacity() + Car.getRemainingCapacity() + Motorcycle.getInitialCapacity();
        }

        public static int getVanOccupiedCount() {
            return Van.getVanOccupancy() + Car.getVanOccupancy();
        }
    }

}