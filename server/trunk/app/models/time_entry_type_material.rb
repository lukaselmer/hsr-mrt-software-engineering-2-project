# Holds information about material which is supposedly used on a time_entry_type
class TimeEntryTypeMaterial < ActiveRecord::Base
  belongs_to :time_entry_type
  belongs_to :material

  validates :material, :time_entry_type, :presence => true

  def to_s
    "#{time_entry_type} (#{amount}x)"
  end
end
