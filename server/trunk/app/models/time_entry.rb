class TimeEntry < ActiveRecord::Base
  belongs_to :customer
  belongs_to :time_entry_type
  belongs_to :gps_position
  belongs_to :user
  belongs_to :order

  #validates :hashcode, :presence => true, :on => :create
  validates :time_start, :time_stop, :presence => true

  def remove_hashcode
    update_attribute(:hashcode, nil)
  end
end
