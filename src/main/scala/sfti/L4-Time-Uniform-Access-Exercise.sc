// uniform access allows us to replace access to instance variables (data)
// with a method which produces that data, hence giving the flexibility to
// modify the internal representation of data without disturbing the contract
// Here, we are taking the Time class developed from the exercise at L4-L5 sheet
// and modifying it to internally store the time as minutes since midnight

class Time(h: Int, m: Int = 0) {
  private var minutesSinceMidnight = h * 60 + m
  def hours = minutesSinceMidnight / 60
  def minutes = minutesSinceMidnight % 60


  // validation when constructing the object can be put as statements
  // in the class body. But is it a right thing to do?
  if (h < 0 || h >= 24 || m < 0 || m >= 60) throw new IllegalArgumentException

  // the updater the of minutes is a little tricky
  def minutes_=(newValue: Int): Unit = {
    if (newValue < 0 || newValue >= 60) throw new IllegalArgumentException
    minutesSinceMidnight = hours * 60 + newValue
  }

  // all of the methods in the class body are instance methods, there is no way
  // to declare a static method here, you need to do that in companion object
  def before(other: Time): Boolean = minutesSinceMidnight < other.minutesSinceMidnight

  override def toString = f"${hours}%02d:${minutes}%02d"
}

// the user of the class is completely unaffected
val morning = new Time(9, 0)
//val crazy = new Time(3, -222)
val afternoon = new Time(16, 30)
morning.before(afternoon)
afternoon.before(morning)

// having auxilliary constructor OR default value
val noon = new Time(12)
noon.hours
noon.minutes
noon.minutes = 30
noon.minutes = 567