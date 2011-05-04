# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ :name => 'Chicago' }, { :name => 'Copenhagen' }])
#   Mayor.create(:name => 'Daley', :city => cities.first)
User.delete_all
#User.create!(:first_name => "Sec", :last_name => "Retary", :email => 'secretary@mrt.ch', :password => 'mrt', :type => User::TYPES[:secretary])
#User.create!(:first_name => "Field", :last_name => "Worker", :email => 'field_worker@mrt.ch', :password => 'mrt', :type => User::TYPES[:field_worker])
Secretary.create!(:first_name => "Sec", :last_name => "Retary", :email => 'secretary@mrt.ch', :password => 'mrt')
FieldWorker.create!(:first_name => "Field", :last_name => "Worker", :email => 'field_worker@mrt.ch', :password => 'mrt')

Customer.delete_all
"Waldemar Lamprecht, Otto Traugott, Hermann Elmo, Veit Ingolf, Björn Simon, 
Klaus Dietfried, Gotthilf Wieland, June Gretchen, Taryn Shelia, Tarah Dione, 
Angelica Xavia, Demontongue Catbroom, Silverbeam Blackmoon, Whirl Gnaw, Nike Medb, 
Pia Ligeia, Ásdís Eirene, Beatrix Afra, Rochus Rocco, Eirene Felicianus, Hrodger Amantius, 
Sigimund Lóegaire, Friðþjófr Ramessu, Yima Philander, Enlil Prabhu, Owain Iovis, Týr Cronus, 
Partha Pramoda, Gawain Jarl, Cupid Amulius".split(',').each do |name|
  f, l = name.strip.split(' ')
  Customer.create!(:first_name => f.strip, :last_name => l.strip)
end


# It may be a good idea to have the same test data as the initial data? The use this command to load all fixtures!
# system("rake db:fixtures:load")



