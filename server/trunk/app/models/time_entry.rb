# Holds information about a time_entry
class TimeEntry < ActiveRecord::Base
  belongs_to :customer
  belongs_to :time_entry_type
  belongs_to :gps_position
  belongs_to :user
  belongs_to :order

  validates :time_start, :time_stop, :presence => true

  scope :unassigned, where("order_id IS NULL OR customer_id IS NULL")

  # removes the hashcode and therefore "validates" the record
  def remove_hashcode
    update_attribute(:hashcode, nil)
  end

  def duration
    Time.at(time_stop - time_start).gmtime
  end
end
