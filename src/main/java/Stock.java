import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Stock {

    public static final int TIME_OF_SELLING = 500;

    Showroom showroom;
    List<Car> cars = new ArrayList<>();
    Lock lock = new ReentrantLock(true);
    Condition condition = lock.newCondition();

    public Stock(Showroom showroom) {
        this.showroom = showroom;
    }

    public Car sellCar() {
        try {
            lock.lock();
            if (cars.isEmpty()) {
                System.out.printf("для клиента %s Нет авто в наличии! ждите\n", Thread.currentThread().getName());
                condition.await();
            }
            System.out.println("Автомобиль " + cars.get(0) + " продан ");
            return cars.remove(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    public void receiveCar(Car car) {
        try {
            lock.lock();
            System.out.println("Приход Авто в австосалон");
            Thread.sleep(TIME_OF_SELLING);
            cars.add(car);
            System.out.printf("на складе %s авто\n", cars.size());
            condition.signal();
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            lock.unlock();
        }

    }

}
