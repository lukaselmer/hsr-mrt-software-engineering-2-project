class TimeEntry < ActiveRecord::Base
  belongs_to :customer
  belongs_to :time_entry_type
  belongs_to :position

  validates :hashcode, :presence => true, :on => :create

  def remove_hashcode
    update_attribute(:hashcode, nil)
  end
end
